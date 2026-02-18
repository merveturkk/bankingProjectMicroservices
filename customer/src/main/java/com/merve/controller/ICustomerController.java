package com.merve.controller;


import com.merve.dto.CreateCustomerRequest;
import com.merve.dto.CustomerResponse;
import com.merve.dto.PageResponse;
import com.merve.dto.UpdateCustomerRequest;
import com.merve.entity.CustomerStatus;
import com.merve.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Customer API", description = "API for managing customer in the system")
public interface ICustomerController {


    @Operation(summary = "Create a new customer", description = "Add a new customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customer data", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                            @ExampleObject(name = "Invalid email format", value = "{\"error\": \"Email must not be empty\"}"),
                            @ExampleObject(name = "Invalid phone number", value = "{\"error\": \"Phone number must be in international format (e.g. +905551112233)\"}")
                    }
            ))
    })
    @PostMapping
    ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request);


    @Operation(summary = "Get a customer by ID", description = "Retrieve a customer using its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"Customer not found\"}")
            ))
    })
    @GetMapping("/{id}")
    ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id);


    @Operation(summary = "List all customers", description = "Retrieve a paginated list of customers")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    @GetMapping
    ResponseEntity<PageResponse<CustomerResponse>> getCustomers(
            @Parameter(description = "Filter by status (e.g., ACTIVE, SUSPENDED)")
            @RequestParam(required = false) CustomerStatus status,
            @PageableDefault(size = 20, sort = "firstName", direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable);


    @Operation(summary = "Update an existing customer", description = "Updates customer details. Requires the current version for optimistic locking.")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    @ApiResponse(responseCode = "409", description = "Conflict - Version mismatch")
    @PutMapping("/{id}")
    ResponseEntity<CustomerResponse> updateCustomer(
            @Valid @RequestBody UpdateCustomerRequest request);


    @Operation(summary = "Soft delete a customer", description = "Changes customer status to SUSPENDED instead of physical deletion.")
    @ApiResponse(responseCode = "204", description = "Customer successfully suspended (No content)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCustomer(@PathVariable UUID id);


}
