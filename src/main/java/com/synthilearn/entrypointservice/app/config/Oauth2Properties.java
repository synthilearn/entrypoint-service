package com.synthilearn.entrypointservice.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "spring.security.oauth2.client.register")
public class Oauth2Properties {

    private final Github github = new Github();
    private String mainPage;

    @Data
    public static class Github {
        private String clientId;
        private String clientSecret;
        private String authLink;
    }
}
