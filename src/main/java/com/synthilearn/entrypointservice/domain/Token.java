package com.synthilearn.entrypointservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private UUID id;
    private UUID customerId;
    private String payload;
    private ZonedDateTime creationDate;
    private Boolean isRevoked;
}
