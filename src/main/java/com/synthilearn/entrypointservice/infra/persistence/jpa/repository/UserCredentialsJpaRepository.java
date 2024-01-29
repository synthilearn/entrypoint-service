package com.synthilearn.entrypointservice.infra.persistence.jpa.repository;

import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.UserCredentialsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserCredentialsJpaRepository extends ReactiveCrudRepository<UserCredentialsEntity, UUID> {

    Mono<UserCredentialsEntity> findByCustomerId(UUID customerId);
    Mono<UserCredentialsEntity> findByEmail(String email);
}
