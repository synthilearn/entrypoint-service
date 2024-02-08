package com.synthilearn.entrypointservice.infra.adapter.client;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.entrypointservice.app.config.WebClientProperties;
import com.synthilearn.entrypointservice.infra.api.rest.dto.CreateWorkspaceRequest;
import com.synthilearn.entrypointservice.infra.api.rest.exception.WorkspaceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkspaceClient {

    private final WebClientProperties webClientProperties;
    private final WebClient webClient;

    public Mono<?> createWorkspace(CreateWorkspaceRequest request) {
        return webClient.post()
                .uri(webClientProperties.getWorkspaceHost() + "/workspace")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateWorkspaceRequest.class)
                .retrieve()
                .bodyToMono(GenericResponse.class)
                .onErrorResume(error -> {
                    log.error("Error has occurred while create workspace with id: {}, exception: {}",
                            request.getId(), error);
                    throw new WorkspaceException(error.getMessage(), 1000);
                });
    }
}
