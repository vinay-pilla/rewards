package com.pilla.rewards.model;

import lombok.Data;

import java.util.Map;

@Data
public class RewardsSummary {
    private String customerId;

    private String customerName;

    private int totalPoints;

    private Map<String, Integer> monthlyPoints;
}
