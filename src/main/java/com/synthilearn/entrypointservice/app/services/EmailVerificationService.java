package com.synthilearn.entrypointservice.app.services;

import com.synthilearn.entrypointservice.domain.EmailVerification;
import com.synthilearn.entrypointservice.domain.VerificationOperation;
import reactor.core.publisher.Mono;

public interface EmailVerificationService {

    Mono<EmailVerification> checkOtp(VerificationOperation operation, String email, String otpCode);
}
