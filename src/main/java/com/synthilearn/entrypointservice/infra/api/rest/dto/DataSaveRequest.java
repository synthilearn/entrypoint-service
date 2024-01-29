package com.synthilearn.entrypointservice.infra.api.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSaveRequest {
    @NotBlank(message = "Name couldn't be empty")
    private String name;
    @NotBlank(message = "Email couldn't be empty")
    @Email(message = "Email isn't valid")
    private String email;
    @NotBlank(message = "Surname couldn't be empty")
    private String surname;
    @NotNull(message = "Birthdate couldn't be null")
    private LocalDate birthDate;
}
