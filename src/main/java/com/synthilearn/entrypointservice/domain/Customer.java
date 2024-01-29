package com.synthilearn.entrypointservice.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Customer {
    private UUID id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
    private RegistrationType registrationType;
    private RegisterStatus status;
    private Role role;
}
