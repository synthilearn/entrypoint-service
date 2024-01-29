package com.synthilearn.entrypointservice.infra.persistence.jpa.repository;

import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.AuthActivityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthActivityJpaRepository extends ReactiveCrudRepository<AuthActivityEntity, UUID> {
}
