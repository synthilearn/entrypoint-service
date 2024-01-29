package com.synthilearn.entrypointservice.infra.persistence.jpa.mapper;


import com.synthilearn.entrypointservice.domain.Customer;
import com.synthilearn.entrypointservice.infra.persistence.jpa.entity.UserCredentialsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserCredentialsEntityMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(target = "id", ignore = true)
    UserCredentialsEntity map(Customer customer, String password);
}
