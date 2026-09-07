package com.assignment.retailerrewardsprogram.exception;

import com.assignment.retailerrewardsprogram.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

/**
 * Handles application exceptions and converts them into
 * consistent REST API error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles requests for customers that do not exist.
     *
     * @param exception customer not found exception
     * @param request current HTTP request
     * @return 404 API error response
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleCustomerNotFound(
            CustomerNotFoundException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * Handles invalid path variable values.
     *
     * @param exception type mismatch exception
     * @param request current HTTP request
     * @return 400 API error response
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid customer ID: " + exception.getValue(),
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    /**
     * Handles invalid request arguments.
     *
     * @param exception illegal argument exception
     * @param request current HTTP request
     * @return 400 API error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }
}