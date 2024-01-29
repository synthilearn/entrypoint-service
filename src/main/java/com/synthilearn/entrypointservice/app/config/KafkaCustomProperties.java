package com.synthilearn.entrypointservice.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "kafka")
public class KafkaCustomProperties {

    private String host;
}
