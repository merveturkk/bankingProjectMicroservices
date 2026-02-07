package com.mycompany.service.customer.exception;


public class OptimisticConflictException extends RuntimeException {

    public OptimisticConflictException() {
        super("Resource was updated by another request");
    }

}
