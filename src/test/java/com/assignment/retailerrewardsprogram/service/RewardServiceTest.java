package com.assignment.retailerrewardsprogram.service;

import com.assignment.retailerrewardsprogram.dto.RewardSummary;
import com.assignment.retailerrewardsprogram.entity.Customer;
import com.assignment.retailerrewardsprogram.entity.Transaction;
import com.assignment.retailerrewardsprogram.exception.CustomerNotFoundException;
import com.assignment.retailerrewardsprogram.repository.CustomerRepository;
import com.assignment.retailerrewardsprogram.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RewardService.
 */
@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RewardCalculationService rewardCalculationService;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void shouldCalculateMonthlyAndTotalRewards() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Anurag Ratna");

        Transaction juneTransaction = new Transaction();
        juneTransaction.setId(1L);
        juneTransaction.setCustomer(customer);
        juneTransaction.setAmount(BigDecimal.valueOf(120));
        juneTransaction.setTransactionDate(
                LocalDate.of(2026, 6, 10)
        );

        Transaction julyTransaction = new Transaction();
        julyTransaction.setId(2L);
        julyTransaction.setCustomer(customer);
        julyTransaction.setAmount(BigDecimal.valueOf(80));
        julyTransaction.setTransactionDate(
                LocalDate.of(2026, 7, 10)
        );

        Transaction augustTransaction = new Transaction();
        augustTransaction.setId(3L);
        augustTransaction.setCustomer(customer);
        augustTransaction.setAmount(BigDecimal.valueOf(150));
        augustTransaction.setTransactionDate(
                LocalDate.of(2026, 8, 10)
        );

        when(rewardCalculationService.calculateRewardPoints(
                BigDecimal.valueOf(150)))
                .thenReturn(150);

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(List.of(
                        juneTransaction,
                        julyTransaction,
                        augustTransaction
                ));

        when(rewardCalculationService.calculateRewardPoints(
                BigDecimal.valueOf(120)))
                .thenReturn(90);

        when(rewardCalculationService.calculateRewardPoints(
                BigDecimal.valueOf(80)))
                .thenReturn(30);

        RewardSummary result =
                rewardService.getRewardSummary(1L);

        assertEquals(1L, result.customerId());
        assertEquals("Anurag Ratna", result.customerName());
        assertEquals(3, result.monthlyRewards().size());
        assertEquals(270, result.totalPoints());
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        when(customerRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> rewardService.getRewardSummary(999L)
        );
    }

    @Test
    void shouldRejectTransactionsOutsideThreeMonthWindow() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Anurag Ratna");

        Transaction januaryTransaction = new Transaction();
        januaryTransaction.setAmount(BigDecimal.valueOf(120));
        januaryTransaction.setTransactionDate(LocalDate.of(2026, 1, 10));

        Transaction aprilTransaction = new Transaction();
        aprilTransaction.setAmount(BigDecimal.valueOf(80));
        aprilTransaction.setTransactionDate(LocalDate.of(2026, 4, 10));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerId(1L))
                .thenReturn(List.of(
                        januaryTransaction,
                        aprilTransaction
                ));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rewardService.getRewardSummary(1L)
        );

        assertEquals(
                "Transactions must fall within a maximum three-month window.",
                exception.getMessage()
        );
    }
}