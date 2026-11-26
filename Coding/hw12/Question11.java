import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

record Transaction(long id, String accountId, BigDecimal amount,
                   String currency, LocalDateTime timestamp) { }

record SettlementLine(long transactionId, String accountId,
                      BigDecimal amountUsd, LocalDate settleDate) { }

class InvalidTransactionException extends RuntimeException {
    InvalidTransactionException(long id, String reason) {
        super("Transaction " + id + ": " + reason);
    }
}

interface ExchangeRateService {
    boolean supports(String currency);

    BigDecimal toUsd(BigDecimal amount, String currency);
}

class TransactionValidationProcessor implements ItemProcessor<Transaction, SettlementLine> {

    private final ExchangeRateService rates;

    TransactionValidationProcessor(ExchangeRateService rates) {
        this.rates = rates;
    }

    @Override
    public SettlementLine process(Transaction tx) {
        if (tx.amount().signum() == 0) {
            return null;                                   // FILTER: zero-amount noise, not an error
        }
        if (tx.accountId() == null || tx.accountId().isBlank()) {
            throw new InvalidTransactionException(tx.id(), "missing account");   // can be SKIPPED
        }
        if (!rates.supports(tx.currency())) {
            throw new InvalidTransactionException(tx.id(), "unsupported currency");
        }
        BigDecimal usd = rates.toUsd(tx.amount(), tx.currency());   // ENRICH: convert the currency
        return new SettlementLine(tx.id(), tx.accountId(), usd, tx.timestamp().toLocalDate());
    }
}

@Configuration
class SettlementStepConfig {

    @Bean
    public Step settlementStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               ItemReader<Transaction> transactionReader,
                               TransactionValidationProcessor processor,
                               ItemWriter<SettlementLine> settlementWriter) {
        return new StepBuilder("settlementStep", jobRepository)
                .<Transaction, SettlementLine>chunk(500, transactionManager)   // 500 items per transaction
                .reader(transactionReader)
                .processor(processor)
                .writer(settlementWriter)
                .faultTolerant()
                .skip(InvalidTransactionException.class)
                .skip(FlatFileParseException.class)
                .skipLimit(100)                            // more than 100 skipped items -> the step FAILS
                .build();
    }
}
