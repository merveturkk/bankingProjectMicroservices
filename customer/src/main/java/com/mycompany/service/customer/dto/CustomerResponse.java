package com.mycompany.service.customer.dto;

import java.time.LocalDate;
import java.util.UUID;


public record CustomerResponse(

        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String address,
        LocalDate dateOfBirth,
        Long version

) {
}
