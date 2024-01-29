package com.synthilearn.entrypointservice.domain.mapper;

import com.synthilearn.entrypointservice.domain.UserCredentials;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.UserCredentialsEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCredentialsMapper {

    UserCredentials map(UserCredentialsEntity entity, String name);
    UserCredentials map(UserCredentialsEntity entity);
}
