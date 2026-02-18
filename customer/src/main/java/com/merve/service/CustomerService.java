package com.merve.service;

import com.merve.dto.*;
import com.merve.entity.Customer;
import com.merve.entity.CustomerStatus;
import com.merve.exception.CustomerAlreadyExistsException;
import com.merve.exception.CustomerNotFoundException;
import com.merve.exception.OptimisticConflictException;
import com.merve.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {

        customerRepository.findByEmail(request.email())
                .ifPresent(c -> {
                    throw new CustomerAlreadyExistsException(request.email());
                });

        Customer customer = customerMapper.toEntity(request);

        Customer createdCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(createdCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponseDto)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> getCustomers(CustomerStatus status, Pageable pageable) {
        CustomerStatus effectiveStatus = (status != null) ? status : CustomerStatus.ACTIVE;

        Page<CustomerResponse> dtoPage = customerRepository
                .findAllByCustomerStatus(effectiveStatus, pageable)
                .map(customerMapper::toResponseDto);
        return PageResponse.of(dtoPage);
    }


    public CustomerResponse updateCustomer(UpdateCustomerRequest request) {

        Customer customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new CustomerNotFoundException(request.id()));

        if (!customer.getVersion().equals(request.version())) {
            throw new OptimisticConflictException();
        }
        customerMapper.updateEntity(request, customer);
        return customerMapper.toResponseDto(customerRepository.save(customer));
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (customer.getCustomerStatus() == CustomerStatus.SUSPENDED) {
            return;
        }

        customer.setCustomerStatus(CustomerStatus.SUSPENDED);
        customer.setUpdatedAt(Instant.now());
    }


}


