package com.merve.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handle(CustomerNotFoundException ex,
                                                   HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(
                        "CUSTOMER_NOT_FOUND",
                        "Customer not found",
                        req.getRequestURI(),
                        Instant.now(),
                        Map.of()

                ));
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handle(CustomerAlreadyExistsException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(
                        "CUSTOMER_ALREADY_EXISTS",
                        ex.getMessage(),
                        req.getRequestURI(),
                        Instant.now(),
                        Map.of()
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                               HttpServletRequest req) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(
                        "INVALID_PATH_PARAMETER",
                        "Invalid parameter: " + ex.getName(),
                        req.getRequestURI(),
                        Instant.now(),
                        Map.of()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest req) {

        return ResponseEntity.badRequest().body(new ApiErrorResponse(
                "MALFORMED_JSON",
                "Request body is invalid or cannot be parsed",
                req.getRequestURI(),
                Instant.now(),
                Map.of()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest req) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(
                "INTERNAL_ERROR",
                "Unexpected error occurred",
                req.getRequestURI(),
                Instant.now(),
                Map.of()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest req) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(
                        "VALIDATION_ERROR",
                        "Validation failed",
                        req.getRequestURI(),
                        Instant.now(),
                        errors
                )
        );
    }

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, OptimisticConflictException.class})
    public ResponseEntity<ApiErrorResponse> handleOptimisticLock(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(
                        "OPTIMISTIC_LOCK",
                        "Resource was updated by another request. Please retry.",
                        req.getRequestURI(),
                        Instant.now(),
                        Map.of()
                )
        );
    }

}






