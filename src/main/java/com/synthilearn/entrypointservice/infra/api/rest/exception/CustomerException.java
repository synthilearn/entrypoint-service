package com.synthilearn.entrypointservice.infra.api.rest.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerException extends RuntimeException {

    private int code;

    public CustomerException(String message, int code) {
        super(message);
        this.code = code;
    }
}
