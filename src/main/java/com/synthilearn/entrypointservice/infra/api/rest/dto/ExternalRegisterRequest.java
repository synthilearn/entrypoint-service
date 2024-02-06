package com.synthilearn.entrypointservice.infra.api.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExternalRegisterRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String name;
}