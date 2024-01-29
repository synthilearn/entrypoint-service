package com.synthilearn.entrypointservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VerificationOperation {
    EMAIL_CONFIRMATION(3),
    PASSWORD_RECOVERY(3),
    EMAIL_RECOVERY(3);
    private final Integer leftAttempts;
}
