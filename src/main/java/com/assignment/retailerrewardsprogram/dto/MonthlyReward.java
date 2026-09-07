package com.assignment.retailerrewardsprogram.dto;

import java.time.YearMonth;

/**
 * Represents reward points earned by a customer during a specific month.
 */
public record MonthlyReward(
        YearMonth month,
        int points
) {
}