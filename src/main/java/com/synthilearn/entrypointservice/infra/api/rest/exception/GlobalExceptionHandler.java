package com.synthilearn.entrypointservice.infra.api.rest.exception;

import com.synthilearn.commonstarter.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GenericResponse<?>> handleValidationException(WebExchangeBindException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return Mono.just(GenericResponse.validError(errors.toString()));
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GenericResponse<?>> handle(CustomerException ex) {
        return Mono.just(GenericResponse.error(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(DecodingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GenericResponse<?>> handle(DecodingException ex) {
        return Mono.just(GenericResponse.error(1000, ex.getMessage()));
    }

    @ExceptionHandler(CredentialsException.class)
    public ResponseEntity<Mono<GenericResponse<?>>> handle(CredentialsException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(Mono.just(GenericResponse.error(ex.getCode(), ex.getMessage())));
    }

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<Mono<GenericResponse<?>>> handle(EmailVerificationException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(Mono.just(GenericResponse.error(ex.getCode(), ex.getMessage())));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Mono<GenericResponse<?>>> handle(TokenException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Mono.just(GenericResponse.error(1000, ex.getMessage())));
    }
}
