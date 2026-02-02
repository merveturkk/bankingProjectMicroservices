package com.mycompany.service.customer.error;

import java.time.Instant;
import java.util.Map;

public record ApiValidationError(
        String code,
        String message,
        String path,
        Instant timestamp,
        Map<String, String> errors
) {}
