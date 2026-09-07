package com.assignment.retailerrewardsprogram.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Calculates reward points based on a transaction amount.
 *
 * <p>The reward calculation follows these rules:</p>
 * <ul>
 *     <li>No points are awarded for amounts up to $50.</li>
 *     <li>One point is awarded for every dollar between $50 and $100.</li>
 *     <li>Two points are awarded for every dollar above $100,
 *     in addition to the first 50 points.</li>
 * </ul>
 */
@Service
public class RewardCalculationService {

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final int POINTS_MULTIPLIER_ABOVE_ONE_HUNDRED = 2;

    /**
     * Calculates reward points for a transaction amount.
     *
     * @param amount transaction amount
     * @return calculated reward points
     * @throws IllegalArgumentException if the amount is null or negative
     */
    public int calculateRewardPoints(BigDecimal amount) {
        validateAmount(amount);

        if (amount.compareTo(FIFTY) <= 0) {
            return 0;
        }

        if (amount.compareTo(ONE_HUNDRED) <= 0) {
            return amount.subtract(FIFTY).intValue();
        }

        return FIFTY.intValue()
                + amount.subtract(ONE_HUNDRED)
                .multiply(BigDecimal.valueOf(POINTS_MULTIPLIER_ABOVE_ONE_HUNDRED))
                .intValue();
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Transaction amount must not be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount must not be negative");
        }
    }
}