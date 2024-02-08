package com.synthilearn.entrypointservice.infra.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkspaceRequest {

    private UUID id;
}
