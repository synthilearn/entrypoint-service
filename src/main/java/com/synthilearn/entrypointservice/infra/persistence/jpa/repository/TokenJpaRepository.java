package com.synthilearn.entrypointservice.infra.persistence.jpa.repository;

import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.TokenEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface TokenJpaRepository extends ReactiveCrudRepository<TokenEntity, UUID> {

    @Query("SELECT * FROM token t where t.customer_id = :customerUUID and t.is_revoked = false and t.payload = :body ORDER BY t.creation_date limit 1")
    Mono<TokenEntity> findValidToken(@Param("customerUUID") UUID customerUUID,
                                     @Param("body") String body);

    @Modifying
    @Query("UPDATE token t SET is_revoked = true WHERE t.customer_id = :customerId")
    Mono<Void> revokeTokens(@Param("customerId") UUID customerId);
}
