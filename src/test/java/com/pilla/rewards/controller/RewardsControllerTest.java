package com.pilla.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pilla.rewards.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("POSITIVE: Calculate rewards for multiple transactions of same customer")
    void testCalculateRewardsWithMultipleTransactions() throws Exception {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("CUST-005", "Carol Davis", 115.00, LocalDate.of(2026, 1, 15)));
        transactions.add(new Transaction("CUST-005", "Carol Davis", 170.00, LocalDate.of(2026, 2, 20)));
        transactions.add(new Transaction("CUST-005", "Carol Davis", 225.00, LocalDate.of(2026, 3, 10)));

        String requestBody = objectMapper.writeValueAsString(transactions);

        // Act & Assert
        mockMvc.perform(post("/api/rewards/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", equalTo("CUST-005")))
                .andExpect(jsonPath("$[0].customerName", equalTo("Carol Davis")))
                .andExpect(jsonPath("$[0].totalPoints", equalTo(570)))
                .andExpect(jsonPath("$[0].monthlyPoints.JANUARY", equalTo(80)))
                .andExpect(jsonPath("$[0].monthlyPoints.FEBRUARY", equalTo(190)))
                .andExpect(jsonPath("$[0].monthlyPoints.MARCH", equalTo(300)));
    }

    @Test
    @DisplayName("POSITIVE: Calculate rewards for single transaction")
    void testCalculateRewardsWithSingleTransaction() throws Exception {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("CUST-001", "John Smith", 120.50, LocalDate.of(2026, 2, 14)));

        String requestBody = objectMapper.writeValueAsString(transactions);

        // Act & Assert
        mockMvc.perform(post("/api/rewards/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", equalTo("CUST-001")))
                .andExpect(jsonPath("$[0].customerName", equalTo("John Smith")))
                .andExpect(jsonPath("$[0].totalPoints", equalTo(91)))
                .andExpect(jsonPath("$[0].monthlyPoints.FEBRUARY", equalTo(91)))
                .andExpect(jsonPath("$[0].monthlyPoints", notNullValue()));
    }

    @Test
    @DisplayName("NEGATIVE: Calculate rewards with empty transaction list")
    void testCalculateRewardsWithEmptyList() throws Exception {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        String requestBody = objectMapper.writeValueAsString(transactions);

        // Act & Assert
        mockMvc.perform(post("/api/rewards/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("Transactions list cannot be empty")));
    }
}
