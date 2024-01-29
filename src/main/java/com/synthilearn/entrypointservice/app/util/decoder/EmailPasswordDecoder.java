package com.synthilearn.entrypointservice.app.util.decoder;

import com.synthilearn.entrypointservice.infra.api.rest.exception.DecodingException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class EmailPasswordDecoder {

    public static EmailPasswordPair decodeSecretPair(String secretPair) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(secretPair);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

            String[] parts = decodedString.split(":");
            if (parts.length == 2) {
                return EmailPasswordPair.builder()
                        .email(parts[0])
                        .password(parts[1])
                        .build();
            }

        } catch (Exception e) {
            log.error("Error has occurred while decoding secretPair: {}, exception: {}", secretPair, e);
        }
        throw new DecodingException("Error has occurred while decoding secretPair");
    }
}
