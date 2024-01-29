package com.synthilearn.entrypointservice.app.services.impl;

import com.synthilearn.entrypointservice.app.port.EmailVerificationRepository;
import com.synthilearn.entrypointservice.app.services.EmailVerificationService;
import com.synthilearn.entrypointservice.domain.EmailVerification;
import com.synthilearn.entrypointservice.domain.VerificationOperation;
import com.synthilearn.entrypointservice.domain.VerificationStatus;
import com.synthilearn.entrypointservice.infra.api.rest.exception.EmailVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public Mono<EmailVerification> checkOtp(VerificationOperation operation, String email, String otpCode) {
        return emailVerificationRepository
                .findInfoByOperationAndEmail(VerificationOperation.EMAIL_CONFIRMATION, email)
                .flatMap(verification -> {
                    updateStatus(verification, otpCode);
                    return Mono.just(verification);
                });
    }

    private void updateStatus(EmailVerification emailVerification, String otpCode) {
        if (emailVerification.getStatus() != VerificationStatus.NOT_USED) {
            throw EmailVerificationException.invalidStatus("Invalid email verification status");
        } else if (emailVerification.getLeftAttempts() <= 0) {
            throw EmailVerificationException.lackAttempts("Lack attempts");
        } else if (emailVerification.getValidTo().isBefore(ZonedDateTime.now())) {
            emailVerificationRepository.updateStatus(emailVerification.getId(), VerificationStatus.CODE_EXPIRED)
                    .subscribe();
            throw EmailVerificationException.expired("Otp code expired");
        } else if (!emailVerification.getEmailOtp().equals(otpCode)) {
            emailVerificationRepository.decrementAttempts(emailVerification.getId(), emailVerification.getLeftAttempts() - 1)
                    .subscribe();
            throw EmailVerificationException.otpDidntMatch("Otp code didn't match");
        }
        emailVerificationRepository.updateStatus(emailVerification.getId(), VerificationStatus.USED)
                .subscribe();
    }
}
