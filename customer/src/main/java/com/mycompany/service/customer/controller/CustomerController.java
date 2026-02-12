package com.mycompany.service.customer.controller;


import com.mycompany.service.customer.dto.CreateCustomerRequest;
import com.mycompany.service.customer.dto.CustomerResponse;
import com.mycompany.service.customer.dto.UpdateCustomerRequest;
import com.mycompany.service.customer.entity.CustomerStatus;
import com.mycompany.service.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<CustomerResponse>> getCustomers(
            @Parameter(description = "Filter by status (e.g., ACTIVE, SUSPENDED)")
            @RequestParam(required = false) CustomerStatus status) {
        return ResponseEntity.ok(customerService.getCustomers(status));
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

