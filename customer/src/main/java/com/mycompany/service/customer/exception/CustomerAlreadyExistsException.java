package com.mycompany.service.customer.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String email) {
        super("Customer with email '" + email + "' already exists");
    }

}
