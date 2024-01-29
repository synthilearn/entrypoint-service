package com.synthilearn.entrypointservice;

import com.synthilearn.entrypointservice.app.config.KafkaCustomProperties;
import com.synthilearn.entrypointservice.app.config.TokenProperties;
import com.synthilearn.entrypointservice.app.config.WebClientProperties;
import com.synthilearn.loggingstarter.EnableLogging;
import com.synthilearn.securestarter.EnableTokenResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableLogging
@EnableTokenResolver
@EnableConfigurationProperties({KafkaCustomProperties.class, WebClientProperties.class, TokenProperties.class})
public class EntrypointServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EntrypointServiceApplication.class, args);
    }

}
