package com.synthilearn.entrypointservice.app.config;

import com.synthilearn.entrypointservice.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class ReactiveKafkaProducerConfig {

    private final KafkaCustomProperties customProperties;

    @Bean
    public ReactiveKafkaProducerTemplate<String, Notification> reactiveKafkaProducerTemplate(KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, customProperties.getHost());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }
}
