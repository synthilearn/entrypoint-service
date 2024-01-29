package com.synthilearn.entrypointservice.app.port;

import com.synthilearn.entrypointservice.domain.Token;
import com.synthilearn.securestarter.AccessToken;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TokenRepository {

    Mono<Token> save(UUID customerId, String refreshToken);

    Mono<Void> revokeAllTokens(UUID customerId);

    Mono<Token> findToken(AccessToken accessToken);
}
