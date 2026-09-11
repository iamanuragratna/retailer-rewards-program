package com.assignment.retailerrewardsprogram;

import com.assignment.retailerrewardsprogram.entity.Customer;
import com.assignment.retailerrewardsprogram.entity.Transaction;
import com.assignment.retailerrewardsprogram.repository.CustomerRepository;
import com.assignment.retailerrewardsprogram.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Full integration test for the reward API.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RewardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

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

    @Test
    void shouldRejectTransactionsOutsideThreeMonthWindow() throws Exception {
        Customer customer = new Customer();
        customer.setName("Anurag Ratna");
        customer = customerRepository.save(customer);

        Transaction januaryTransaction = new Transaction();
        januaryTransaction.setCustomer(customer);
        januaryTransaction.setAmount(BigDecimal.valueOf(120));
        januaryTransaction.setTransactionDate(LocalDate.of(2026, 1, 10));

        Transaction aprilTransaction = new Transaction();
        aprilTransaction.setCustomer(customer);
        aprilTransaction.setAmount(BigDecimal.valueOf(80));
        aprilTransaction.setTransactionDate(LocalDate.of(2026, 4, 10));

        transactionRepository.save(januaryTransaction);
        transactionRepository.save(aprilTransaction);

        mockMvc.perform(get("/api/v1/rewards/" + customer.getId()))
                .andExpect(status().isBadRequest());
    }
}