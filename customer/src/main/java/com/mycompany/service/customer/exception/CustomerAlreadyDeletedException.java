package com.mycompany.service.customer.exception;

import java.util.UUID;

public class CustomerAlreadyDeletedException extends RuntimeException{

    public CustomerAlreadyDeletedException(UUID id) {
        super("Customer with id " + id + " is already deleted");
    }
}
