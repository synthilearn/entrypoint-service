package com.synthilearn.entrypointservice.infra.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubClientResponse {

    private String login;
    private String name;
}
