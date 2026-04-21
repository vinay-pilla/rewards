package com.pilla.rewards.controller;

import com.pilla.rewards.config.TransactionMockData;
import com.pilla.rewards.model.RewardsSummary;
import com.pilla.rewards.model.Transaction;
import com.pilla.rewards.service.RewardsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rewards")
public class RewardsController {

    private final RewardsService rewardsService;
    private final TransactionMockData transactionMockData;

    @GetMapping("/calculate")
    public ResponseEntity<List<RewardsSummary>> getRewardsForMockData() {
        List<List<com.pilla.rewards.model.Transaction>> transactions = Collections.singletonList(transactionMockData.getMockTransactions());
        List<RewardsSummary> summaries = rewardsService.calculateRewards(transactions);
        return ResponseEntity.ok(summaries);
    }

    @PostMapping("/calculate")
    public ResponseEntity<List<RewardsSummary>> calculateRewards(
            @Valid @RequestBody List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            throw new IllegalArgumentException("Transactions list cannot be empty");
        }
        List<RewardsSummary> summaries = rewardsService.calculateRewards(Collections.singletonList(transactions));
        return ResponseEntity.ok(summaries);
    }
}
