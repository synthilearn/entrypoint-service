package com.synthilearn.entrypointservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthActivity {

    private UUID id;
    private UUID credentialId;
    private String device;
    private String ip;
    private ZonedDateTime expiredAt;
}
