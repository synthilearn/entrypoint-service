package com.synthilearn.entrypointservice.infra.api.rest.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class CredentialsException extends RuntimeException {

    private final HttpStatus status;
    private final int code;

    private CredentialsException(String message, HttpStatus status, int code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static CredentialsException notFound(String message) {
        return new CredentialsException(message, HttpStatus.NOT_FOUND, 1000);
    }

    public static CredentialsException locked(String message) {
        return new CredentialsException(message, HttpStatus.BAD_REQUEST, 4000);
    }

    public static CredentialsException passwordsNonMatched(String message) {
        return new CredentialsException(message, HttpStatus.BAD_REQUEST, 1000);
    }
}
