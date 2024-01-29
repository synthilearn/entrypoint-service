package com.synthilearn.entrypointservice.app.services.impl;

import com.synthilearn.entrypointservice.app.config.TokenProperties;
import com.synthilearn.entrypointservice.app.services.TokenProcessor;
import com.synthilearn.entrypointservice.domain.TokenPair;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class TokenProcessorImpl implements TokenProcessor {

    private final TokenProperties tokenProperties;

    @Override
    public TokenPair generateTokenPair(String email, String customerId) {
        String accessToken = generateToken(email, customerId, tokenProperties.getExpiration().getAccess());
        String refreshToken = generateToken(email, customerId, tokenProperties.getExpiration().getRefresh());
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateToken(String email, String customerId, Integer expiration) {
        PrivateKey privateKey = getPrivateKey();

        Instant expirationTime = Instant.now().plusSeconds(expiration);

        return Jwts.builder()
                .setSubject(email)
                .setId(customerId)
                .setExpiration(Date.from(expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(tokenProperties.getPrivateKey());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error("Error loading private key: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
