package com.synthilearn.entrypointservice.app.port.impl;

import com.synthilearn.entrypointservice.app.port.UserCredentialsRepository;
import com.synthilearn.entrypointservice.domain.Customer;
import com.synthilearn.entrypointservice.domain.UserCredentials;
import com.synthilearn.entrypointservice.domain.mapper.UserCredentialsMapper;
import com.synthilearn.entrypointservice.infra.api.rest.exception.CredentialsException;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.UserCredentialsEntity;
import com.synthilearn.entrypointservice.infra.persistence.jpa.mapper.UserCredentialsEntityMapper;
import com.synthilearn.entrypointservice.infra.persistence.jpa.repository.UserCredentialsJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCredentialsRepositoryImpl implements UserCredentialsRepository {

    private final UserCredentialsJpaRepository userCredentialsJpaRepository;
    private final UserCredentialsEntityMapper userCredentialsEntityMapper;
    private final UserCredentialsMapper userCredentialsMapper;

    @Override
    @Transactional
    public Mono<UserCredentials> initialSave(Customer customer, String password) {
        return userCredentialsJpaRepository.save(init(customer, password))
                .map(entity -> userCredentialsMapper.map(entity, customer.getName()));
    }

    @Override
    public Mono<UserCredentials> findByCustomerId(UUID customerId) {
        return userCredentialsJpaRepository.findById(customerId)
                .map(userCredentialsMapper::map);
    }

    @Override
    public Mono<UserCredentials> findByEmail(String email) {
        return userCredentialsJpaRepository.findByEmail(email)
                .map(userCredentialsMapper::map);
    }

    @Override
    @Transactional
    public Mono<UserCredentials> activateByCustomerUUID(UUID customerId) {
        return userCredentialsJpaRepository.findByCustomerId(customerId)
                .singleOptional()
                .flatMap(credentialsOpt -> {
                    UserCredentialsEntity credentials = credentialsOpt
                            .orElseThrow(() -> CredentialsException.notFound("Credentials not found for customerId: " + customerId));

                    credentials.setIsLocked(false);
                    return userCredentialsJpaRepository.save(credentials)
                            .map(userCredentialsMapper::map);
                });
    }

    private UserCredentialsEntity init(Customer customer, String password) {
        return userCredentialsEntityMapper.map(customer, password)
                .toBuilder()
                .id(UUID.randomUUID())
                .creationDate(ZonedDateTime.now())
                .updatedDate(ZonedDateTime.now())
                .newRecord(true)
                .remember(true)
                .isLocked(true)
                .build();
    }
}

