package com.assignment.retailerrewardsprogram.service;

import com.assignment.retailerrewardsprogram.dto.MonthlyReward;
import com.assignment.retailerrewardsprogram.dto.RewardSummary;
import com.assignment.retailerrewardsprogram.entity.Customer;
import com.assignment.retailerrewardsprogram.entity.Transaction;
import com.assignment.retailerrewardsprogram.exception.CustomerNotFoundException;
import com.assignment.retailerrewardsprogram.repository.CustomerRepository;
import com.assignment.retailerrewardsprogram.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for calculating and aggregating customer rewards.
 */
@Service
public class RewardService {
    private static final int MAX_REWARD_MONTHS = 3;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final RewardCalculationService rewardCalculationService;

    public RewardService(
            CustomerRepository customerRepository,
            TransactionRepository transactionRepository,
            RewardCalculationService rewardCalculationService) {

        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.rewardCalculationService = rewardCalculationService;
    }

    /**
     * Calculates the monthly and total reward points for a customer.
     *
     * <p>Transactions are expected to fall within a maximum three-month
     * reporting window. The window is determined dynamically from the
     * transaction dates and is not tied to specific calendar months.</p>
     *
     * @param customerId customer identifier
     * @return reward summary
     * @throws CustomerNotFoundException if the customer does not exist
     * @throws IllegalArgumentException  if transactions exceed a three-month window
     */
    public RewardSummary getRewardSummary(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(customerId));

        List<Transaction> transactions =
                transactionRepository.findByCustomerId(customerId);

        validateThreeMonthWindow(transactions);

        Map<YearMonth, Integer> pointsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction ->
                                YearMonth.from(transaction.getTransactionDate()),
                        Collectors.summingInt(transaction ->
                                rewardCalculationService.calculateRewardPoints(
                                        transaction.getAmount()))
                ));

        List<MonthlyReward> monthlyRewards = pointsByMonth.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        new MonthlyReward(
                                entry.getKey(),
                                entry.getValue()))
                .toList();

        int totalPoints = monthlyRewards.stream()
                .mapToInt(MonthlyReward::points)
                .sum();

        return new RewardSummary(
                customer.getId(),
                customer.getName(),
                monthlyRewards,
                totalPoints
        );
    }

    /**
     * Validates that all customer transactions fall within a maximum
     * three-calendar-month reporting window.
     *
     * @param transactions customer transactions
     * @throws IllegalArgumentException if transactions span more than three months
     */
    private void validateThreeMonthWindow(List<Transaction> transactions) {

        if (transactions.isEmpty()) {
            return;
        }

        YearMonth earliestMonth = transactions.stream()
                .map(transaction -> YearMonth.from(transaction.getTransactionDate()))
                .min(YearMonth::compareTo)
                .orElseThrow();

        YearMonth latestMonth = transactions.stream()
                .map(transaction -> YearMonth.from(transaction.getTransactionDate()))
                .max(YearMonth::compareTo)
                .orElseThrow();

        long monthsBetween = ChronoUnit.MONTHS.between(
                earliestMonth,
                latestMonth
        );

        if (monthsBetween >= MAX_REWARD_MONTHS) {
            throw new IllegalArgumentException("Transactions must fall within a maximum three-month window.");
        }
    }
}