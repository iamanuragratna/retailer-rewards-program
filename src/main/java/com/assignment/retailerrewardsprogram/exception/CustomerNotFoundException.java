package com.assignment.retailerrewardsprogram.exception;

/**
 * Exception thrown when a requested customer does not exist.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with id: " + customerId);
    }
}