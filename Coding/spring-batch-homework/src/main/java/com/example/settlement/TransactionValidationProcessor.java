package com.example.settlement;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidationProcessor implements ItemProcessor<Transaction, SettlementLine> {
    private final ExchangeRateService rates;

    public TransactionValidationProcessor(ExchangeRateService rates) {
        this.rates = rates;
    }

    @Override
    public SettlementLine process(Transaction tx) {
        if (tx.amount().compareTo(BigDecimal.ZERO) == 0) {
            return null; // Filter zero-amount transactions.
        }
        if (tx.accountId() == null || tx.accountId().isBlank()) {
            throw new InvalidTransactionException(tx.id(), "account ID is missing or blank");
        }
        if (!rates.supports(tx.currency())) {
            throw new InvalidTransactionException(tx.id(), "unsupported currency: " + tx.currency());
        }
        return new SettlementLine(tx.id(), tx.accountId(),
                rates.toUsd(tx.amount(), tx.currency()), tx.timestamp().toLocalDate());
    }
}
