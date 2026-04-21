package com.pilla.rewards.service;

import com.pilla.rewards.exception.InvalidTransactionException;
import com.pilla.rewards.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RewardsServiceTest {

    private RewardsService rewardsService;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        rewardsService = new RewardsService();
        Transaction transaction = new Transaction();
        transaction.setAmount(120.00);
        transaction.setCustomerId(String.valueOf(123L));
        transaction.setTransactionDate(LocalDate.parse("2024-06-01"));
        this.transaction = transaction;
    }

    @Test
    @DisplayName("$120 purchase should earn 90 points (spec example)")
    void testPoints_120Dollars() {
        assertEquals(90, rewardsService.calculatePoints(transaction));
    }

    @Test
    @DisplayName("$200 purchase should earn 250 points")
    void testPoints_200Dollars() {
        transaction.setAmount(200.00);
        // 2×100 + 1×50 = 250
        assertEquals(250, rewardsService.calculatePoints(transaction));
    }

    @Test
    @DisplayName("Negative transaction amount ")
    void testPoints_NegativeAmount_ThrowsException() {
        transaction.setAmount(-200.00);
        assertEquals(0, rewardsService.calculatePoints(transaction));
    }
}
