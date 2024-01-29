package com.synthilearn.entrypointservice.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "security.token")
public class TokenProperties {
    private String privateKey;
    private Expiration expiration = new Expiration();

    @Data
    public static class Expiration {
        private Integer access;
        private Integer refresh;
    }
}
