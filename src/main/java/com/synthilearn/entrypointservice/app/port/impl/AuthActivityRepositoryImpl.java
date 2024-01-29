package com.synthilearn.entrypointservice.app.port.impl;

import com.synthilearn.entrypointservice.app.port.AuthActivityRepository;
import com.synthilearn.entrypointservice.domain.AuthActivity;
import com.synthilearn.entrypointservice.domain.UserCredentials;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.AuthActivityEntity;
import com.synthilearn.entrypointservice.infra.persistence.jpa.mapper.AuthActivityEntityMapper;
import com.synthilearn.entrypointservice.infra.persistence.jpa.repository.AuthActivityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthActivityRepositoryImpl implements AuthActivityRepository {

    private final AuthActivityJpaRepository authActivityJpaRepository;
    private final AuthActivityEntityMapper authActivityEntityMapper;


    @Override
    @Transactional
    public Mono<AuthActivity> save(UserCredentials userCredentials, String ip, String device) {
        return authActivityJpaRepository.save(init(userCredentials, ip, device))
                .map(authActivityEntityMapper::map);
    }

    private AuthActivityEntity init(UserCredentials userCredentials, String ip, String device) {
        return AuthActivityEntity.builder()
                .device(device)
                .id(UUID.randomUUID())
                .credentialId(userCredentials.getId())
                .creationDate(ZonedDateTime.now())
                .ip(ip)
                .newRecord(true)
                .build();
    }
}
