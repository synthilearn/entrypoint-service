package com.synthilearn.entrypointservice.infra.adapter.client;

import com.synthilearn.commonstarter.GenericResponse;
import com.synthilearn.entrypointservice.app.config.WebClientProperties;
import com.synthilearn.entrypointservice.domain.Customer;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ActivateRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.DataSaveRequest;
import com.synthilearn.entrypointservice.infra.api.rest.dto.ExternalRegisterRequest;
import com.synthilearn.entrypointservice.infra.api.rest.exception.CustomerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerClient {

    private final WebClientProperties webClientProperties;

    private final WebClient webClient;

    public Mono<Customer> dataSave(DataSaveRequest request) {
        return webClient.post()
                .uri(webClientProperties.getCustomerHost() + "/auth/save-data")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), DataSaveRequest.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Customer>>() {
                })
                .map(GenericResponse::getResultData)
                .onErrorResume(error -> {
                    log.error("Error has occurred while save data for customer: {}, exception: {}",
                            request, error);
                    GenericResponse<?> response = ((WebClientResponseException) error).getResponseBodyAs(GenericResponse.class);
                    throw new CustomerException(response.getMessage(), response.getCode());
                });
    }

    public Mono<Customer> activate(ActivateRequest request) {
        return webClient.post()
                .uri(webClientProperties.getCustomerHost() + "/auth/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ActivateRequest.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Customer>>() {
                })
                .map(GenericResponse::getResultData)
                .onErrorResume(error -> {
                    log.error("Error has occurred while activate customer: {}, exception: {}",
                            request, error);
                    GenericResponse<?> response = ((WebClientResponseException) error).getResponseBodyAs(GenericResponse.class);
                    throw new CustomerException(response.getMessage(), response.getCode());
                });
    }

    public Mono<Customer> findByEmail(String email) {
        return webClient.get()
                .uri(webClientProperties.getCustomerHost() + "/customer/by-email/{email}", email)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Customer>>() {
                })
                .map(GenericResponse::getResultData)
                .onErrorResume(error -> {
                    log.error("Error has occurred while find customer with email: {}, exception: {}",
                            email, error);
                    GenericResponse<?> response = ((WebClientResponseException) error).getResponseBodyAs(GenericResponse.class);
                    throw new CustomerException(response.getMessage(), response.getCode());
                });
    }

    public Mono<Customer> externalRegister(ExternalRegisterRequest request) {
        return webClient.post()
                .uri(webClientProperties.getCustomerHost() + "/auth/external-register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ExternalRegisterRequest.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Customer>>() {
                })
                .map(GenericResponse::getResultData)
                .onErrorResume(error -> {
                    log.error("Error has occurred while external register customer with email: {}, exception: {}",
                            request.getEmail(), error);
                    GenericResponse<?> response = ((WebClientResponseException) error).getResponseBodyAs(GenericResponse.class);
                    throw new CustomerException(response.getMessage(), response.getCode());
                });
    }
}
