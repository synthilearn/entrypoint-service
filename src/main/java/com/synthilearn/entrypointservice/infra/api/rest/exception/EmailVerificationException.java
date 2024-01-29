package com.synthilearn.entrypointservice.infra.api.rest.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailVerificationException extends RuntimeException {

    private final HttpStatus status;
    private final int code;

    private EmailVerificationException(String message, HttpStatus status, int code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static EmailVerificationException notFound(String message) {
        return new EmailVerificationException(message, HttpStatus.NOT_FOUND, 1000);
    }

    public static EmailVerificationException lackAttempts(String message) {
        return new EmailVerificationException(message, HttpStatus.BAD_REQUEST, 3050);
    }

    public static EmailVerificationException expired(String message) {
        return new EmailVerificationException(message, HttpStatus.BAD_REQUEST, 3055);
    }

    public static EmailVerificationException otpDidntMatch(String message) {
        return new EmailVerificationException(message, HttpStatus.BAD_REQUEST, 3060);
    }

    public static EmailVerificationException invalidStatus(String message) {
        return new EmailVerificationException(message, HttpStatus.BAD_REQUEST, 3065);
    }
}
