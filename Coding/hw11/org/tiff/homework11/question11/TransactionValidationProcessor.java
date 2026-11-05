package org.tiff.homework11.question11;

import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class TransactionValidationProcessor implements ItemProcessor<Transaction, SettlementLine> {
    private final ExchangeRateService rates;

    public TransactionValidationProcessor(ExchangeRateService rates) {
        this.rates = rates;
    }

    @Override
    public SettlementLine process(Transaction tx) {
        if (tx.amount().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        if (tx.accountId() == null || tx.accountId().isBlank()) {
            throw new InvalidTransactionException(tx.id(), "Account id is missing");
        }
        if (!rates.supports(tx.currency())) {
            throw new InvalidTransactionException(tx.id(), "Unsupported currency");
        }

        return new SettlementLine(tx.id(), tx.accountId(),
                rates.toUsd(tx.amount(), tx.currency()), tx.timestamp().toLocalDate());
    }
}
