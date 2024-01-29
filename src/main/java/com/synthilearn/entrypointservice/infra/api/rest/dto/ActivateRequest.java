package com.synthilearn.entrypointservice.infra.api.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRequest {

    @NotBlank(message = "Email couldn't be empty")
    @Email(message = "Email isn't valid")
    private String email;
    @NotBlank(message = "otpCode couldn't be empty")
    private String otpCode;
}
