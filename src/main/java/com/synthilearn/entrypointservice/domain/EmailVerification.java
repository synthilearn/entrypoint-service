package com.synthilearn.entrypointservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.ZonedDateTime;
import java.util.UUID;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification {

    private UUID id;
    private UUID credentialsId;
    private ZonedDateTime validTo;
    private String email;
    private VerificationStatus status;
    private Integer leftAttempts;
    private VerificationOperation operation;
    private String emailOtp;
}
