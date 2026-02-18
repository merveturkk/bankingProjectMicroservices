package com.merve.exception;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Error response model for API errors")
public record ErrorResponse(
        @Schema(description = "Error message", example = "Customer not found")
        String error
) {
}
