
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

// Input transaction
record Transaction(
        long id,
        String accountId,
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp) {
}

// Output settlement record
record SettlementLine(
        long transactionId,
        String accountId,
        BigDecimal amountUsd,
        LocalDate settleDate) {
}

// Custom validation exception
class InvalidTransactionException extends RuntimeException {

    InvalidTransactionException(long id, String reason) {
        super("Transaction " + id + ": " + reason);
    }
}

// Exchange rate service
interface ExchangeRateService {

    boolean supports(String currency);

    BigDecimal toUsd(BigDecimal amount, String currency);
}

// Transaction processor
class TransactionValidationProcessor
        implements ItemProcessor<Transaction, SettlementLine> {

    private final ExchangeRateService rates;

    TransactionValidationProcessor(ExchangeRateService rates) {
        this.rates = rates;
    }

    @Override
    public SettlementLine process(Transaction tx) {

        // 1. Filter transactions with zero amount
        if (tx.amount().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        // 2. Validate account ID
        if (tx.accountId() == null ||
                tx.accountId().isBlank()) {

            throw new InvalidTransactionException(
                    tx.id(), "Account ID is null or blank");
        }

        // 3. Validate currency
        if (!rates.supports(tx.currency())) {

            throw new InvalidTransactionException(
                    tx.id(), "Unsupported currency: " + tx.currency());
        }

        // 4. Convert transaction amount to USD
        BigDecimal amountUsd =
                rates.toUsd(tx.amount(), tx.currency());

        // 5. Extract date from transaction timestamp
        LocalDate settleDate = tx.timestamp().toLocalDate();

        // 6. Create settlement record
        return new SettlementLine(
                tx.id(),
                tx.accountId(),
                amountUsd,
                settleDate
        );
    }
}

// Spring Batch step configuration
@Configuration
class SettlementStepConfig {

    @Bean
    public TransactionValidationProcessor transactionValidationProcessor(
            ExchangeRateService rates) {

        return new TransactionValidationProcessor(rates);
    }

    @Bean
    public Step settlementStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<Transaction> transactionReader,
            TransactionValidationProcessor processor,
            ItemWriter<SettlementLine> settlementWriter) {

        return new StepBuilder("settlementStep", jobRepository)
                .<Transaction, SettlementLine>chunk(500, transactionManager)
                .reader(transactionReader)
                .processor(processor)
                .writer(settlementWriter)
                .faultTolerant()
                .skip(InvalidTransactionException.class)
                .skip(FlatFileParseException.class)
                .skipLimit(100)
                .build();
    }
}
