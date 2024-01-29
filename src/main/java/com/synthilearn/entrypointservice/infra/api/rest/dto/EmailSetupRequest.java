package com.synthilearn.entrypointservice.infra.api.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailSetupRequest {

    @NotBlank(message = "Email couldn't be empty")
    @Email(message = "Email isn't valid")
    private String email;
}
