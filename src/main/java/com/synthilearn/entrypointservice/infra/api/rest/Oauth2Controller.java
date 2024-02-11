package com.synthilearn.entrypointservice.infra.api.rest;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.entrypointservice.app.config.Oauth2Properties;
import com.synthilearn.entrypointservice.app.services.Oauth2Service;
import com.synthilearn.entrypointservice.infra.api.rest.dto.Oauth2Links;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/entrypoint-service/v1/oauth2")
public class Oauth2Controller {

    private final Oauth2Properties oauth2Properties;
    private final Oauth2Service oauth2Service;

    @GetMapping("/github/auth-link")
    public Mono<GenericResponse<Oauth2Links>> getGithubLink() {
        Map<String, String> links = new HashMap<>();
        links.put("github", oauth2Properties.getGithub().getAuthLink());
        return Mono.just(new Oauth2Links(links))
                .map(GenericResponse::ok);
    }

    @GetMapping("/github/auth")
    public Mono<Void> githubAuthorization(@RequestParam("code") String code,
                                          ServerWebExchange exchange) {
        String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
        String platform = exchange.getRequest().getHeaders().getFirst(HttpHeaders.USER_AGENT);

        return oauth2Service.githubAuthorization(code, ip, platform)
                .flatMap(tokenPair -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FOUND);
                    response.getHeaders().setLocation(URI.create(oauth2Properties.getMainPage() +
                            "?access_token=" + tokenPair.getAccessToken() +
                            "&refresh_token=" + tokenPair.getRefreshToken()));
                    return Mono.empty();
                }).then();
    }
}
