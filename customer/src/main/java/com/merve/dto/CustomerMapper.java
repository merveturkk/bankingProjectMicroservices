package com.merve.dto;


import com.merve.entity.Customer;
import com.merve.entity.CustomerStatus;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CustomerMapper {


    Customer toEntity(CreateCustomerRequest request);

    @AfterMapping
    default void afterCreatedMapping(@MappingTarget Customer customer) {
        customer.setCustomerStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(Instant.now());
        customer.setUpdatedAt(Instant.now());
    }


    CustomerResponse toResponseDto(Customer customer);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "customerStatus", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(UpdateCustomerRequest request, @MappingTarget Customer customer);

    @AfterMapping
    default void setAuditFieldsAfterUpdate(@MappingTarget Customer customer) {
        customer.setUpdatedAt(Instant.now());
    }


}

