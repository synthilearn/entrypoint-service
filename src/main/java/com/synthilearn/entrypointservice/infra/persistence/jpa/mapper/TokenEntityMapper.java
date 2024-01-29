package com.synthilearn.entrypointservice.infra.persistence.jpa.mapper;


import com.synthilearn.entrypointservice.domain.Token;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.TokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenEntityMapper {

    Token map(TokenEntity entity);
}
