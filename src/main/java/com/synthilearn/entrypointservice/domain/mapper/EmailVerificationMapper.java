package com.synthilearn.entrypointservice.domain.mapper;

import com.synthilearn.entrypointservice.domain.EmailVerification;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.EmailVerificationInfoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailVerificationMapper {

    EmailVerification map(EmailVerificationInfoEntity entity);
}
