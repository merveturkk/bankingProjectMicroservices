package com.mycompany.service.customer.error;

import java.time.Instant;

public record ApiErrorResponse(
        String code,
        String message,
        String path,
        Instant timestamp
) {}