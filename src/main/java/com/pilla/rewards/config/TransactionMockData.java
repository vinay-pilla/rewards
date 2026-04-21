package com.pilla.rewards.config;

import com.pilla.rewards.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TransactionMockData {

    public List<Transaction> getMockTransactions() {
        return List.of(
                new Transaction("CUST-001", "Alice Johnson", 120.00, LocalDate.of(2026, 1, 5)),
                // Jan: $45  → 0 pts (below $50 threshold)
                new Transaction("CUST-001", "Alice Johnson", 45.00, LocalDate.of(2026, 1, 18)),
                // Feb: $75  → 1×25 = 25 pts
                new Transaction("CUST-001", "Alice Johnson", 75.00, LocalDate.of(2026, 2, 3)),
                // Feb: $200 → 2×100 + 1×50 = 250 pts
                new Transaction("CUST-001", "Alice Johnson", 200.00, LocalDate.of(2026, 2, 20)),
                // Mar: $50  → 0 pts (exactly $50, threshold is exclusive)
                new Transaction("CUST-001", "Alice Johnson", 50.00, LocalDate.of(2026, 3, 10)),
                // Mar: $110 → 2×10 + 1×50 = 70 pts
                new Transaction("CUST-001", "Alice Johnson", 110.00, LocalDate.of(2026, 3, 25)),

                // (CUST-002) ────────────────────────────────────────────
                // Jan: $30  → 0 pts
                new Transaction("CUST-002", "Bob Smith", 30.00, LocalDate.of(2026, 1, 7)),
                // Jan: $150 → 2×50 + 1×50 = 150 pts
                new Transaction("CUST-002", "Bob Smith", 150.00, LocalDate.of(2026, 1, 22)),
                // Feb: $60  → 1×10 = 10 pts
                new Transaction("CUST-002", "Bob Smith", 60.00, LocalDate.of(2026, 2, 14)),
                // Feb: $95  → 1×45 = 45 pts
                new Transaction("CUST-002", "Bob Smith", 95.00, LocalDate.of(2026, 2, 28)),
                // Mar: $300 → 2×200 + 1×50 = 450 pts
                new Transaction("CUST-002", "Bob Smith", 300.00, LocalDate.of(2026, 3, 15)),

                // ── Carol (CUST-003) ──────────────────────────────────────────
                // Jan: $80  → 1×30 = 30 pts
                new Transaction("CUST-003", "Carol Davis", 80.00, LocalDate.of(2026, 1, 3)),
                // Jan: $100 → 1×50 = 50 pts (exactly $100, upper threshold is exclusive)
                new Transaction("CUST-003", "Carol Davis", 100.00, LocalDate.of(2026, 1, 19)),
                // Feb: $130 → 2×30 + 1×50 = 110 pts
                new Transaction("CUST-003", "Carol Davis", 130.00, LocalDate.of(2026, 2, 8)),
                // Mar: $20  → 0 pts
                new Transaction("CUST-003", "Carol Davis", 20.00, LocalDate.of(2026, 3, 5)),
                // Mar: $175 → 2×75 + 1×50 = 200 pts
                new Transaction("CUST-003", "Carol Davis", 175.00, LocalDate.of(2026, 3, 28))
        );
    }
}
