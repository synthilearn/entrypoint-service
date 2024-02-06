package com.synthilearn.entrypointservice.app.services.impl;

import com.synthilearn.entrypointservice.app.port.UserCredentialsRepository;
import com.synthilearn.entrypointservice.app.services.AuthService;
import com.synthilearn.entrypointservice.app.services.Oauth2Service;
import com.synthilearn.entrypointservice.domain.TokenPair;
import com.synthilearn.entrypointservice.infra.adapter.client.CustomerClient;
import com.synthilearn.entrypointservice.infra.adapter.client.GithubClient;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ExternalRegisterRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.GitHubTokenResponse;
import com.synthilearn.entrypointservice.infra.api.rest.dto.GithubClientEmail;
import com.synthilearn.entrypointservice.infra.api.rest.dto.GithubClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2ServiceImpl implements Oauth2Service {

    private final GithubClient githubClient;
    private final AuthService authService;

    @Override
    public Mono<TokenPair> githubAuthorization(String code, String ip, String platform) {
        Mono<GitHubTokenResponse> authTokenMono = githubClient.requestAccessToken(code);
        return authTokenMono.flatMap(token -> {
            Mono<List<GithubClientEmail>> emailsMono = githubClient.getClientEmails(token.getAccessToken());
            Mono<GithubClientResponse> clientDataMono = githubClient.getClientData(token.getAccessToken());
            return emailsMono.zipWith(clientDataMono)
                    .flatMap(tuple -> {
                        List<GithubClientEmail> emails = tuple.getT1();
                        GithubClientResponse data = tuple.getT2();
                        String primaryEmail = findPrimaryEmail(emails);
                        return authService
                                .externalLogin(ExternalRegisterRequest.builder()
                                        .email(primaryEmail)
                                        .name(StringUtils.isEmpty(data.getName()) ? data.getLogin() : data.getName())
                                        .build(), ip, platform);
                    });
        });
    }

    private String findPrimaryEmail(List<GithubClientEmail> emails) {
        return emails.stream()
                .filter(GithubClientEmail::getPrimary)
                .map(GithubClientEmail::getEmail)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
