package com.mycompany.service.customer.service;

import com.mycompany.service.customer.dto.CustomerResponse;
import com.mycompany.service.customer.dto.CreateCustomerRequest;
import com.mycompany.service.customer.dto.UpdateCustomerRequest;
import com.mycompany.service.customer.entity.Customer;
import com.mycompany.service.customer.entity.CustomerStatus;
import com.mycompany.service.customer.exception.CustomerAlreadyDeletedException;
import com.mycompany.service.customer.exception.CustomerAlreadyExistsException;
import com.mycompany.service.customer.exception.CustomerNotFoundException;
import com.mycompany.service.customer.mapper.CustomerMapper;
import com.mycompany.service.customer.repository.CustomerRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponseDto)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersActive() {
        return customerRepository.findAllByCustomerStatus(CustomerStatus.ACTIVE)
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getCustomersByStatus(CustomerStatus status) {
        return customerRepository.findAllByCustomerStatus(status)
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();
    }




    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerMapper.updateEntity(request,customer);
        customer.setUpdatedAt(Instant.now());

        return customerMapper.toResponseDto(customerRepository.save(customer));

    }



    @Transactional
    public void deleteCustomer(UUID id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (customer.getCustomerStatus() == CustomerStatus.SUSPENDED) {
            throw new CustomerAlreadyDeletedException(id);

        }
        customer.setCustomerStatus(CustomerStatus.SUSPENDED);
        customer.setUpdatedAt(Instant.now());
    }
}


