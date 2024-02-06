package com.synthilearn.entrypointservice.app.services;

import com.synthilearn.entrypointservice.domain.TokenPair;
import reactor.core.publisher.Mono;

public interface Oauth2Service {
    Mono<TokenPair> githubAuthorization(String code, String ip, String platform);
}
