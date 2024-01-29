package com.synthilearn.entrypointservice.infra.persistence.jpa.repository;

import com.synthilearn.entrypointservice.app.port.EmailVerificationRepository;
import com.synthilearn.entrypointservice.domain.VerificationOperation;
import com.synthilearn.entrypointservice.domain.VerificationStatus;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.EmailVerificationInfoEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface EmailVerificationJpaRepository extends ReactiveCrudRepository<EmailVerificationInfoEntity, UUID> {

    Mono<EmailVerificationInfoEntity> findTopByOperationAndEmailOrderByCreationDateDesc(VerificationOperation operation,
                                                                                     String email);

    @Modifying
    @Query("UPDATE email_verification_info SET status = :status WHERE id = :id")
    Mono<Void> updateStatus(UUID id, VerificationStatus status);

    @Modifying
    @Query("UPDATE email_verification_info SET left_attempts = :leftAttempts WHERE id = :id")
    Mono<Void> decrementAttempts(UUID id, Integer leftAttempts);
}
