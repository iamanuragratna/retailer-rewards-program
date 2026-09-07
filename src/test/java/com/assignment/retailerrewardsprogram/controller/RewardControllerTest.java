package com.assignment.retailerrewardsprogram.controller;

import com.assignment.retailerrewardsprogram.dto.MonthlyReward;
import com.assignment.retailerrewardsprogram.dto.RewardSummary;
import com.assignment.retailerrewardsprogram.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.YearMonth;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for RewardController.
 */
@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;

    @Test
    void shouldReturnRewardSummary() throws Exception {

        RewardSummary summary = new RewardSummary(
                1L,
                "Anurag Ratna",
                List.of(
                        new MonthlyReward(
                                YearMonth.of(2026, 6),
                                120
                        )
                ),
                120
        );

        when(rewardService.getRewardSummary(1L))
                .thenReturn(summary);

        mockMvc.perform(get("/api/v1/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Anurag Ratna"))
                .andExpect(jsonPath("$.monthlyRewards[0].points").value(120))
                .andExpect(jsonPath("$.totalPoints").value(120));
    }

    @Test
    void shouldReturnBadRequestForNegativeCustomerId() throws Exception {

        mockMvc.perform(get("/api/v1/rewards/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForNonNumericCustomerId() throws Exception {

        mockMvc.perform(get("/api/v1/rewards/abc"))
                .andExpect(status().isBadRequest());
    }
}
