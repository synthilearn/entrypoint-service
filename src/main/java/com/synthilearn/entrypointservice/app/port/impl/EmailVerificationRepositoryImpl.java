package com.synthilearn.entrypointservice.app.port.impl;

import com.synthilearn.entrypointservice.app.port.EmailVerificationRepository;
import com.synthilearn.entrypointservice.domain.EmailVerification;
import com.synthilearn.entrypointservice.domain.VerificationOperation;
import com.synthilearn.entrypointservice.domain.VerificationStatus;
import com.synthilearn.entrypointservice.domain.mapper.EmailVerificationMapper;
import com.synthilearn.entrypointservice.infra.api.rest.exception.EmailVerificationException;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.EmailVerificationInfoEntity;
import com.synthilearn.entrypointservice.infra.persistence.jpa.mapper.EmailVerificationEntityMapper;
import com.synthilearn.entrypointservice.infra.persistence.jpa.repository.EmailVerificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {

    private final EmailVerificationJpaRepository emailVerificationJpaRepository;
    private final EmailVerificationEntityMapper emailVerificationEntityMapper;
    private final EmailVerificationMapper emailVerificationMapper;


    @Override
    @Transactional
    public Mono<EmailVerification> save(EmailVerification emailVerification, String otpCode, UUID credentialsId) {
        return emailVerificationJpaRepository.save(init(emailVerification, otpCode, credentialsId))
                .map(emailVerificationMapper::map);
    }

    @Override
    public Mono<EmailVerification> findInfoByOperationAndEmail(VerificationOperation operation, String email) {
        return emailVerificationJpaRepository.
                findTopByOperationAndEmailOrderByCreationDateDesc(operation, email)
                .switchIfEmpty(Mono.defer(() -> {
                    String errorMessage = String.format("Email verification with operation %s and email: %s not found",
                            operation, email);
                    return Mono.error(EmailVerificationException
                        .notFound(errorMessage));
                }))
                .map(emailVerificationMapper::map);
    }

    @Override
    @Transactional
    public Mono<Void> updateStatus(UUID uuid, VerificationStatus status) {
        return emailVerificationJpaRepository.updateStatus(uuid, status);
    }

    @Override
    @Transactional
    public Mono<Void> decrementAttempts(UUID uuid, int actualAttempts) {
        return emailVerificationJpaRepository.decrementAttempts(uuid, actualAttempts);
    }

    private EmailVerificationInfoEntity init(EmailVerification emailVerification, String otpCode, UUID credentialsId) {
        return emailVerificationEntityMapper.map(emailVerification, otpCode)
                .toBuilder()
                .id(UUID.randomUUID())
                .creationDate(ZonedDateTime.now())
                .updatedDate(ZonedDateTime.now())
                .credentialsId(credentialsId)
                .emailOtp(otpCode)
                .status(VerificationStatus.NOT_USED)
                .newRecord(true)
                .leftAttempts(emailVerification.getOperation().getLeftAttempts())
                .build();
    }
}
