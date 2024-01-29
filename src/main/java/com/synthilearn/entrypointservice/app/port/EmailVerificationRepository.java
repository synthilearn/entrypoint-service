package com.synthilearn.entrypointservice.app.port;

import com.synthilearn.entrypointservice.domain.EmailVerification;
import com.synthilearn.entrypointservice.domain.VerificationOperation;
import com.synthilearn.entrypointservice.domain.VerificationStatus;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EmailVerificationRepository {

        Mono<EmailVerification> save(EmailVerification emailVerification, String otpCode);
        Mono<EmailVerification> findInfoByOperationAndEmail(VerificationOperation operation, String email);
        Mono<Void> updateStatus(UUID uuid, VerificationStatus status);
        Mono<Void> decrementAttempts(UUID uuid, int actualAttempts);
}
