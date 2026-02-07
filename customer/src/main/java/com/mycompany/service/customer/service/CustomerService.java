package com.mycompany.service.customer.service;

import com.mycompany.service.customer.dto.CreateCustomerRequest;
import com.mycompany.service.customer.dto.CustomerMapper;
import com.mycompany.service.customer.dto.CustomerResponse;
import com.mycompany.service.customer.dto.UpdateCustomerRequest;
import com.mycompany.service.customer.entity.Customer;
import com.mycompany.service.customer.entity.CustomerStatus;
import com.mycompany.service.customer.exception.CustomerAlreadyExistsException;
import com.mycompany.service.customer.exception.CustomerNotFoundException;
import com.mycompany.service.customer.exception.OptimisticConflictException;
import com.mycompany.service.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<CustomerResponse> getCustomers(CustomerStatus status) {

        CustomerStatus effectiveStatus = (status != null) ? status : CustomerStatus.ACTIVE;

        return customerRepository.findAllByCustomerStatus(effectiveStatus)
                .stream()
                .map(customerMapper::toResponseDto)
                .toList();
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


