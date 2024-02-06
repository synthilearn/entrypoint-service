package com.synthilearn.entrypointservice.infra.api.rest;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.entrypointservice.app.services.AuthService;
import com.synthilearn.entrypointservice.domain.Customer;
import com.synthilearn.entrypointservice.domain.TokenPair;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ActivateRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.DataSaveRequest;
import com.synthilearn.securestarter.AccessToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/entrypoint-service/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/data-save")
    public Mono<GenericResponse<Customer>> dataSave(@RequestBody @Valid DataSaveRequest request,
                                                    @RequestHeader("secretPair") String secretPair) {
        return authService.dataSave(request, secretPair)
                .map(GenericResponse::ok);
    }

    @PostMapping("/activate")
    public Mono<GenericResponse<Customer>> activate(@RequestBody @Valid ActivateRequest request) {
        return authService.activate(request)
                .map(GenericResponse::ok);
    }

    @PostMapping("/login")
    public Mono<GenericResponse<TokenPair>> login(@RequestHeader("secretPair") String secretPair,
                                                  @RequestHeader(value = "ip", required = false) String ip,
                                                  @RequestHeader(value = "platform", required = false) String device) {
        return authService.login(secretPair, ip, device)
                .map(GenericResponse::ok);
    }

    @PostMapping("/refresh")
    public Mono<GenericResponse<TokenPair>> refresh(AccessToken accessToken) {
        return authService.refresh(accessToken)
                .map(GenericResponse::ok);
    }
}

