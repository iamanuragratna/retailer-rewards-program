package com.assignment.retailerrewardsprogram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full integration test for the reward API.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RewardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSeededCustomerRewards() throws Exception {

        mockMvc.perform(get("/api/v1/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Anurag Ratna"))
                .andExpect(jsonPath("$.totalPoints").value(332));
    }

    @Test
    void shouldReturnNotFoundForUnknownCustomer() throws Exception {

        mockMvc.perform(get("/api/v1/rewards/999"))
                .andExpect(status().isNotFound());
    }
}