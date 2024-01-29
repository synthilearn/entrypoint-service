package com.synthilearn.entrypointservice.infra.persistence.jpa.mapper;

import com.synthilearn.entrypointservice.domain.AuthActivity;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.AuthActivityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthActivityEntityMapper {

    AuthActivity map(AuthActivityEntity entity);
}
