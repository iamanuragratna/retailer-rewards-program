package com.assignment.retailerrewardsprogram.repository;

import com.assignment.retailerrewardsprogram.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing customer data.
 * <p>
 * Provides standard CRUD operations for the Customer entity
 * through Spring Data JPA.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
