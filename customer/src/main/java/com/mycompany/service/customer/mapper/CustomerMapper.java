package com.mycompany.service.customer.mapper;


import com.mycompany.service.customer.dto.CreateCustomerRequest;
import com.mycompany.service.customer.dto.CustomerResponse;
import com.mycompany.service.customer.dto.UpdateCustomerRequest;
import com.mycompany.service.customer.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    //RequestDto to Entity
    Customer toEntity(CreateCustomerRequest request);

    // Entity to ResponseDto
    CustomerResponse toResponseDto(Customer customer);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "customerStatus", ignore = true)
    void updateEntity(UpdateCustomerRequest request, @MappingTarget Customer customer);




}

