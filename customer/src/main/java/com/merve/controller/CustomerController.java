package com.merve.controller;


import com.merve.dto.CreateCustomerRequest;
import com.merve.dto.CustomerResponse;
import com.merve.dto.PageResponse;
import com.merve.dto.UpdateCustomerRequest;
import com.merve.entity.CustomerStatus;
import com.merve.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController implements ICustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }


    @Override
    public ResponseEntity<PageResponse<CustomerResponse>> getCustomers(
            CustomerStatus status,
            Pageable pageable) {
        PageResponse<CustomerResponse> response = customerService.getCustomers(status, pageable);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Valid @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(request));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}

