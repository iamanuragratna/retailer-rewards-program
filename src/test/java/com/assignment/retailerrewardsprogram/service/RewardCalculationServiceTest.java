package com.assignment.retailerrewardsprogram.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for reward point calculation.
 */
class RewardCalculationServiceTest {

    private final RewardCalculationService service =
            new RewardCalculationService();

    @Test
    void shouldReturnZeroForAmountBelowFifty() {
        assertEquals(
                0,
                service.calculateRewardPoints(BigDecimal.valueOf(49))
        );
    }

    @Test
    void shouldReturnZeroForExactlyFifty() {
        assertEquals(
                0,
                service.calculateRewardPoints(BigDecimal.valueOf(50))
        );
    }

    @Test
    void shouldReturnOnePointForAmountOfFiftyOne() {
        assertEquals(
                1,
                service.calculateRewardPoints(BigDecimal.valueOf(51))
        );
    }

    @Test
    void shouldReturnFortyNinePointsForNinetyNine() {
        assertEquals(
                49,
                service.calculateRewardPoints(BigDecimal.valueOf(99))
        );
    }

    @Test
    void shouldReturnFiftyPointsForExactlyOneHundred() {
        assertEquals(
                50,
                service.calculateRewardPoints(BigDecimal.valueOf(100))
        );
    }

    @Test
    void shouldReturnFiftyTwoPointsForOneHundredOne() {
        assertEquals(
                52,
                service.calculateRewardPoints(BigDecimal.valueOf(101))
        );
    }

    @Test
    void shouldCalculatePointsAboveOneHundred() {
        assertEquals(
                90,
                service.calculateRewardPoints(BigDecimal.valueOf(120))
        );
    }

    @Test
    void shouldReturnZeroForZeroAmount() {
        assertEquals(
                0,
                service.calculateRewardPoints(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculateRewardPoints(
                        BigDecimal.valueOf(-1)
                )
        );
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.calculateRewardPoints(null)
        );
    }
}