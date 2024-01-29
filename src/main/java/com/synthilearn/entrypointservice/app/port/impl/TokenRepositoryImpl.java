package com.synthilearn.entrypointservice.app.port.impl;

import com.synthilearn.entrypointservice.app.config.TokenProperties;
import com.synthilearn.entrypointservice.app.port.TokenRepository;
import com.synthilearn.entrypointservice.domain.Token;
import com.synthilearn.entrypointservice.infra.api.rest.exception.TokenException;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.TokenEntity;
import com.synthilearn.entrypointservice.infra.persistence.jpa.mapper.TokenEntityMapper;
import com.synthilearn.entrypointservice.infra.persistence.jpa.repository.TokenJpaRepository;
import com.synthilearn.securestarter.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenJpaRepository tokenJpaRepository;
    private final TokenEntityMapper tokenEntityMapper;
    private final TokenProperties tokenProperties;

    @Override
    @Transactional
    public Mono<Token> save(UUID customerId, String refreshToken) {
        return tokenJpaRepository.save(initRefreshToken(customerId, refreshToken))
                .map(tokenEntityMapper::map);
    }

    @Override
    @Transactional
    public Mono<Void> revokeAllTokens(UUID customerId) {
        return tokenJpaRepository.revokeTokens(customerId);
    }

    @Override
    public Mono<Token> findToken(AccessToken accessToken) {
        return tokenJpaRepository.findValidToken(accessToken.getId(), accessToken.getBody())
                .singleOptional()
                .map(tokenOpt -> {
                    TokenEntity tokenEntity = tokenOpt
                            .orElseThrow(() -> TokenException.notFound("Valid token not found for customerId: " + accessToken.getId()));
                    return tokenEntityMapper.map(tokenEntity);
                });
    }

    private TokenEntity initRefreshToken(UUID customerId, String refreshToken) {
        return TokenEntity.builder()
                .creationDate(ZonedDateTime.now())
                .id(UUID.randomUUID())
                .customerId(customerId)
                .payload(refreshToken)
                .expiredAt(ZonedDateTime.now().plusSeconds(tokenProperties.getExpiration().getRefresh()))
                .isRevoked(false)
                .newRecord(true)
                .build();
    }
}
