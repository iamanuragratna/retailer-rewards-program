package com.assignment.retailerrewardsprogram.dto;

import java.util.List;

/**
 * Represents the complete reward summary for a customer.
 */
public record RewardSummary(
        Long customerId,
        String customerName,
        List<MonthlyReward> monthlyRewards,
        int totalPoints
) {
}