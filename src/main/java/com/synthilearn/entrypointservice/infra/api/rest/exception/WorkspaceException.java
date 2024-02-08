package com.synthilearn.entrypointservice.infra.api.rest.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkspaceException extends RuntimeException {
    private int code;

    public WorkspaceException(String message, int code) {
        super(message);
        this.code = code;
    }
}
