package com.synthilearn.entrypointservice.app.util.decoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailPasswordPair {
    private String email;
    private String password;
}
