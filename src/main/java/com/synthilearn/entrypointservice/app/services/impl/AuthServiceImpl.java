package com.synthilearn.entrypointservice.app.services.impl;

import com.synthilearn.entrypointservice.app.port.AuthActivityRepository;
import com.synthilearn.entrypointservice.app.port.EmailVerificationRepository;
import com.synthilearn.entrypointservice.app.port.TokenRepository;
import com.synthilearn.entrypointservice.app.port.UserCredentialsRepository;
import com.synthilearn.entrypointservice.app.services.AuthService;
import com.synthilearn.entrypointservice.app.services.EmailVerificationService;
import com.synthilearn.entrypointservice.app.services.TokenProcessor;
import com.synthilearn.entrypointservice.app.util.OtpGenerator;
import com.synthilearn.entrypointservice.app.util.decoder.EmailPasswordDecoder;
import com.synthilearn.entrypointservice.app.util.decoder.EmailPasswordPair;
import com.synthilearn.entrypointservice.domain.*;
import com.synthilearn.entrypointservice.infra.adapter.client.CustomerClient;
import com.synthilearn.entrypointservice.infra.adapter.client.WorkspaceClient;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ActivateRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.CreateWorkspaceRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.DataSaveRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ExternalRegisterRequest;
import com.synthilearn.entrypointservice.infra.api.rest.exception.CredentialsException;
import com.synthilearn.securestarter.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerClient customerClient;
    private final UserCredentialsRepository userCredentialsRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationService emailVerificationService;
    private final AuthActivityRepository authActivityRepository;
    private final TokenProcessor tokenService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final WorkspaceClient workspaceClient;
    private final ReactiveKafkaProducerTemplate<String, Notification> producerTemplate;

    @Override
    @Transactional
    public Mono<Customer> dataSave(DataSaveRequest request, String secretPair) {
        return customerClient.dataSave(request)
                .flatMap(customer -> {
                    String encryptedPassword = passwordEncoder.encode(EmailPasswordDecoder.decodeSecretPair(secretPair).getPassword());
                    return userCredentialsRepository.initialSave(customer, encryptedPassword, true)
                            .flatMap(userCredentials -> {
                                String otpCode = OtpGenerator.generateOTP();
                                var emailVerification = fillEmailVerification(userCredentials, VerificationOperation.EMAIL_CONFIRMATION);
                                return emailVerificationRepository.save(emailVerification, otpCode, userCredentials.getId())
                                        .map(savedEmailVerification -> {
                                            producerTemplate.send(emailVerification.getOperation().name(), formEmailConfirnationNotification(userCredentials, otpCode))
                                                    .subscribe();
                                            return customer;
                                        });
                            });
                });
    }

    @Override
    public Mono<Customer> activate(ActivateRequest request) {
        return customerClient.findByEmail(request.getEmail())
                .flatMap(customer -> emailVerificationService
                        .checkOtp(VerificationOperation.EMAIL_CONFIRMATION, request.getEmail(), request.getOtpCode())
                        .flatMap(verification -> {
                            userCredentialsRepository.activateByCustomerUUID(customer.getId())
                                    .subscribe();
                            workspaceClient.createWorkspace(new CreateWorkspaceRequest(customer.getId()))
                                    .subscribe();
                            return customerClient.activate(request);
                        }));
    }

    @Override
    @Transactional
    public Mono<TokenPair> login(String secretPair, String ip, String device) {
        EmailPasswordPair emailPasswordPair = EmailPasswordDecoder.decodeSecretPair(secretPair);
        return userCredentialsRepository.findByEmail(emailPasswordPair.getEmail())
                .singleOptional()
                .flatMap(userCredentialsOpt -> {
                    UserCredentials userCredentials = userCredentialsOpt
                            .orElseThrow(() -> CredentialsException.notFound("Credentials not found for email: " + emailPasswordPair.getEmail()));
                    checkUserCredentialsOnLocked(userCredentials);
                    checkPasswordMatched(userCredentials, emailPasswordPair.getPassword());
                    TokenPair tokenPair = tokenService.generateTokenPair(userCredentials.getEmail(), userCredentials.getCustomerId().toString());
                    tokenRepository.save(userCredentials.getCustomerId(), tokenPair.getRefreshToken())
                            .subscribe();
                    authActivityRepository.save(userCredentials, ip, device)
                            .subscribe();
                    return Mono.just(tokenPair);
                });
    }

    @Override
    public Mono<TokenPair> refresh(AccessToken accessToken) {
        return tokenRepository.findToken(accessToken)
                .map(token -> {
                    TokenPair tokenPair = tokenService.generateTokenPair(accessToken.getEmail(), accessToken.getId().toString());
                    tokenRepository.revokeAllTokens(accessToken.getId())
                            .subscribe();
                    tokenRepository.save(accessToken.getId(), tokenPair.getRefreshToken())
                            .subscribe();
                    return tokenPair;
                });
    }

    @Override
    @Transactional
    public Mono<TokenPair> externalLogin(ExternalRegisterRequest request, String ip, String device) {
        return userCredentialsRepository.findByEmail(request.getEmail())
                .singleOptional()
                .flatMap(userCredentialsOpt -> {
                    Mono<UserCredentials> userCredentialsMono;
                    if (userCredentialsOpt.isEmpty()) {
                        userCredentialsMono = customerClient.externalRegister(request)
                                .flatMap(customer -> {
                                    workspaceClient.createWorkspace(new CreateWorkspaceRequest(customer.getId()))
                                            .subscribe();
                                    return userCredentialsRepository.initialSave(customer, null, false);
                                });
                    } else {
                        UserCredentials userCredentials = userCredentialsOpt.get();
                        checkUserCredentialsOnLocked(userCredentials);
                        userCredentialsMono = Mono.just(userCredentials);
                    }

                    return userCredentialsMono.flatMap(userCredentials -> {
                        TokenPair tokenPair = tokenService.generateTokenPair(userCredentials.getEmail(), userCredentials.getCustomerId().toString());
                        return tokenRepository.save(userCredentials.getCustomerId(), tokenPair.getRefreshToken())
                                .then(authActivityRepository.save(userCredentials, ip, device))
                                .thenReturn(tokenPair);
                    });
                });
    }

    private void checkUserCredentialsOnLocked(UserCredentials userCredentials) {
        if (userCredentials.getIsLocked()) {
            throw CredentialsException.locked("Otp code doesn't verified");
        }
    }

    private void checkPasswordMatched(UserCredentials userCredentials, String password) {
        if (!passwordEncoder.matches(password, userCredentials.getPassword())) {
            throw CredentialsException.passwordsNonMatched("Passwords don't match");
        }
    }

    private EmailVerification fillEmailVerification(UserCredentials userCredentials, VerificationOperation operation) {
        return EmailVerification.builder()
                .credentialsId(userCredentials.getId())
                .email(userCredentials.getEmail())
                .operation(operation)
                .validTo(ZonedDateTime.now().plusDays(3))
                .build();
    }

    private Notification formEmailConfirnationNotification(UserCredentials userCredentials, String optCode) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", userCredentials.getEmail());
        payload.put("name", userCredentials.getName());
        payload.put("code", optCode);
        return new Notification(payload);
    }
}
