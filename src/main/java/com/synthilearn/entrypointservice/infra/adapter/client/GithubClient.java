package com.synthilearn.entrypointservice.infra.adapter.client;

import com.synthilearn.entrypointservice.app.config.Oauth2Properties;
import com.synthilearn.entrypointservice.infra.api.rest.dto.GitHubTokenResponse;
import com.synthilearn.entrypointservice.infra.api.rest.dto.GithubClientEmail;
import com.synthilearn.entrypointservice.infra.api.rest.dto.GithubClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubClient {

    private final Oauth2Properties oauth2Properties;
    private final WebClient webClient;

    public Mono<GitHubTokenResponse> requestAccessToken(String code) {
        return webClient.post()
                .uri("https://github.com/login/oauth/access_token" +
                        "?client_id=" + oauth2Properties.getGithub().getClientId() +
                        "&client_secret=" + oauth2Properties.getGithub().getClientSecret() +
                        "&code=" + code)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GitHubTokenResponse.class)
                .onErrorResume(error -> {
                    String message = String.format("Error has occurred while obtaining github access token with code: %s, error: %s", code, error);
                    log.error(message);
                    return Mono.error(new RuntimeException(message));
                });
    }

    public Mono<GithubClientResponse> getClientData(String token) {
        return webClient.get()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GithubClientResponse.class)
                .onErrorResume(error -> {
                    String message = String.format("Error has occurred while obtaining github client data with token: %s, error: %s", token, error);
                    log.error(message);
                    return Mono.error(new RuntimeException(message));
                });
    }

    public Mono<List<GithubClientEmail>> getClientEmails(String token) {
        return webClient.get()
                .uri("https://api.github.com/user/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GithubClientEmail>>() {})
                .onErrorResume(error -> {
                    String message = String.format("Error has occurred while obtaining github client emails with token: %s, error: %s", token, error);
                    log.error(message);
                    return Mono.error(new RuntimeException(message));
                });
    }

}
