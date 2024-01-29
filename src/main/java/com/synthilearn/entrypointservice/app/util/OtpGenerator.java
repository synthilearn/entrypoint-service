package com.synthilearn.entrypointservice.app.util;

import java.util.Random;

public class OtpGenerator {

    public static String generateOTP() {
        Random random = new Random();

        StringBuilder otpCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            otpCode.append(digit);
        }

        return otpCode.toString();
    }
}
