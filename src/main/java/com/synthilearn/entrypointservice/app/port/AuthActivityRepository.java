package com.synthilearn.entrypointservice.app.port;

import com.synthilearn.entrypointservice.domain.AuthActivity;
import com.synthilearn.entrypointservice.domain.UserCredentials;
import reactor.core.publisher.Mono;

public interface AuthActivityRepository {

    Mono<AuthActivity> save(UserCredentials userCredentials, String ip, String device);
}
