package com.assignment.retailerrewardsprogram.dto;

import java.time.LocalDateTime;

/**
 * Represents a standard API error response.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}