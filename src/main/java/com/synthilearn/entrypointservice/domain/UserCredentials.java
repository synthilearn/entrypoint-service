package com.synthilearn.entrypointservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserCredentials {

    private UUID id;
    private UUID customerId;
    private String email;
    private Boolean remember;
    private Boolean isLocked;
    private String password;
    @JsonIgnore
    private String name;
}
