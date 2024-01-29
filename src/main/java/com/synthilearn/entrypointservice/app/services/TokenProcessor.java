package com.synthilearn.entrypointservice.app.services;

import com.synthilearn.entrypointservice.domain.TokenPair;
import com.synthilearn.entrypointservice.domain.UserCredentials;

public interface TokenProcessor {

    TokenPair generateTokenPair(String email, String customerId);
}
