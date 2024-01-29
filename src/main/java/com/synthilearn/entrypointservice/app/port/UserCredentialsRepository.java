package com.synthilearn.entrypointservice.app.port;

import com.synthilearn.entrypointservice.domain.Customer;
import com.synthilearn.entrypointservice.domain.UserCredentials;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserCredentialsRepository {

    Mono<UserCredentials> initialSave(Customer customer, String password);
    Mono<UserCredentials> findByCustomerId(UUID customerId);
    Mono<UserCredentials> findByEmail(String email);
    Mono<UserCredentials> activateByCustomerUUID(UUID customerId);
}
