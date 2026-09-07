package com.assignment.retailerrewardsprogram.repository;

import com.assignment.retailerrewardsprogram.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for accessing transaction data.
 * <p>
 * Provides standard CRUD operations for the Transaction entity
 * through Spring Data JPA.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    /**
     * Finds all transactions belonging to a customer.
     *
     * @param customerId customer identifier
     * @return customer's transactions
     */
    List<Transaction> findByCustomerId(Long customerId);
}
