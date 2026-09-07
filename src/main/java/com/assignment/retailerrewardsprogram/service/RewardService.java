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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for calculating and aggregating customer rewards.
 */
@Service
public class RewardService {

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
     * @param customerId customer identifier
     * @return reward summary
     * @throws IllegalArgumentException if the customer does not exist
     */
    public RewardSummary getRewardSummary(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                new CustomerNotFoundException(customerId));

        List<Transaction> transactions =
                transactionRepository.findByCustomerId(customerId);

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
}