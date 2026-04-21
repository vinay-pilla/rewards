package com.pilla.rewards.service;

import com.pilla.rewards.model.RewardsSummary;
import com.pilla.rewards.model.Transaction;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    public List<RewardsSummary> calculateRewards(List<List<Transaction>> transactions) {
        return transactions.stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Transaction::getCustomerId))
                .entrySet().stream()
                .map(entry -> {
                    String customerId = entry.getKey();
                    List<Transaction> customerTransactions = entry.getValue();
                    String customerName = customerTransactions.get(0).getCustomerName();

                    int totalPoints = customerTransactions.stream()
                            .mapToInt(this::calculatePoints)
                            .sum();

                    Map<String, Integer> monthlyPoints = customerTransactions.stream()
                            .collect(Collectors.groupingBy(
                                    t -> t.getTransactionDate().getMonth().toString(),
                                    Collectors.summingInt(this::calculatePoints)
                            ));

                    RewardsSummary summary = new RewardsSummary();
                    summary.setCustomerId(customerId);
                    summary.setCustomerName(customerName);
                    summary.setTotalPoints(totalPoints);
                    summary.setMonthlyPoints(monthlyPoints);
                    return summary;
                })
                .collect(Collectors.toList());
    }

     int calculatePoints(Transaction transaction) {
        double amount = transaction.getAmount();
        int points = 0;

        if (amount > 100) {
            points += (int) (((amount - 100) * 2) + 50); // 2 points for every dollar above $100 + 1 point for every dollar between $50 and $100
        } else if (amount > 50) {
            points += (int) ((amount - 50)); // 1 point for every dollar between $50 and $100
        }

        return points;
    }

}
