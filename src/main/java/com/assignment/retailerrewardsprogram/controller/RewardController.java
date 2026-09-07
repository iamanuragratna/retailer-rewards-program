package com.assignment.retailerrewardsprogram.controller;

import com.assignment.retailerrewardsprogram.dto.RewardSummary;
import com.assignment.retailerrewardsprogram.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for customer reward information.
 */
@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Returns the monthly and total reward points for a customer.
     *
     * @param customerId customer identifier
     * @return reward summary
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardSummary> getRewards(
            @PathVariable Long customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException(
                    "Customer ID must be greater than zero");
        }

        return ResponseEntity.ok(
                rewardService.getRewardSummary(customerId)
        );
    }
}