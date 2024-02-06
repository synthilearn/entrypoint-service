package com.synthilearn.entrypointservice.app.services;

import com.synthilearn.entrypointservice.domain.Customer;
import com.synthilearn.entrypointservice.domain.TokenPair;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ActivateRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.DataSaveRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ExternalRegisterRequest;
import com.synthilearn.securestarter.AccessToken;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<Customer> dataSave(DataSaveRequest request, String secretPair);
    Mono<Customer> activate(ActivateRequest request);
    Mono<TokenPair> login(String secretPair, String ip, String device);
    Mono<TokenPair> externalLogin(ExternalRegisterRequest request, String ip, String device);
    Mono<TokenPair> refresh(AccessToken accessToken);
}
