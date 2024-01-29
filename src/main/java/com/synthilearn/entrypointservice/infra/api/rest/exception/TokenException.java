package com.synthilearn.entrypointservice.infra.api.rest.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TokenException extends RuntimeException {

    private TokenException(String message) {
        super(message);
    }

    public static TokenException notFound(String message) {
        return new TokenException(message);
    }
}
