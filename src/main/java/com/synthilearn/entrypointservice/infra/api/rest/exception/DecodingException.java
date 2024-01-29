package com.synthilearn.entrypointservice.infra.api.rest.exception;

public class DecodingException extends RuntimeException {
    public DecodingException(String message) {
        super(message);
    }
}
