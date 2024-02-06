package com.synthilearn.entrypointservice.infra.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubClientEmail {

    private String email;
    private Boolean primary;
    private Boolean verified;
}
