package com.synthilearn.entrypointservice.infra.persistence.jpa.mapper;

import com.synthilearn.entrypointservice.domain.EmailVerification;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.EmailVerificationInfoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailVerificationEntityMapper {

    EmailVerificationInfoEntity map(EmailVerification emailVerification, String emailOtp);
}
