package com.mycompany.service.customer.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mycompany.service.customer.dto.CreateCustomerRequest;
import com.mycompany.service.customer.dto.CustomerResponse;
import com.mycompany.service.customer.dto.UpdateCustomerRequest;
import com.mycompany.service.customer.entity.Customer;
import com.mycompany.service.customer.entity.CustomerStatus;
import com.mycompany.service.customer.exception.CustomerAlreadyExistsException;
import com.mycompany.service.customer.mapper.CustomerMapper;
import com.mycompany.service.customer.repository.CustomerRepository;
import jakarta.validation.constraints.*;
import org.apache.kafka.common.message.UpdateFeaturesResponseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
@DisplayName("Customer service unit test")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService; // this is what we want to test


    private Customer testCustomerEntity;
    private CustomerStatus testCustomerStatus;
    private CreateCustomerRequest testCustomerRequest;
    private CustomerResponse testCustomerResponse;
    private UpdateCustomerRequest testUpdateRequest;

    UUID customerId;
    Instant now;


    @BeforeEach
    void setUp() {

        customerId = UUID.randomUUID();
        now = Instant.parse("2026-02-02T10:00:00Z");

        testCustomerRequest = CreateCustomerRequest.builder()
                .firstName("Merve")
                .lastName("Turk")
                .email("merve@mail.com")
                .phoneNumber("+31612345678")
                .password("Aa1!aaaa")
                .address("Utrecth")
                .dateOfBirth(LocalDate.parse("1995-06-02"))
                .build();


        testUpdateRequest = UpdateCustomerRequest.builder()
                .firstName("Merve")
                .lastName("Turk")
                .email("merve@mail.com")
                .phoneNumber("+31612345678")
                .address("Utrecth")
                .dateOfBirth(LocalDate.parse("1995-06-02"))
                .build();

        customerEntity = new Customer();
        customerEntity.setId(customerId);
        customerEntity.setCustomerStatus(CustomerStatus.ACTIVE);
        customerEntity.setCreatedAt(now);
        customerEntity.setUpdatedAt(now);

        customerResponse = new CustomerResponse();
        customerResponse.setId(customerId.toString());
        customerResponse.setCustomerStatus(CustomerStatus.ACTIVE.name());
        customerResponse.setCreatedAt(now.toString());
    }




    }


    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new CustomerAlreadyExistsException(request.getEmail());
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setCustomerStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(Instant.now());
        customer.setUpdatedAt(Instant.now());
        Customer createdCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(createdCustomer);
    }


}