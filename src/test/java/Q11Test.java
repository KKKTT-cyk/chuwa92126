import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.support.ResourcelessJobRepository;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Q11Test {
    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2026, 10, 26, 23, 59);

    private final TransactionValidationProcessor processor = new TransactionValidationProcessor(
            new ExchangeRateService() {
                @Override
                public boolean supports(String currency) {
                    return "EUR".equals(currency);
                }

                @Override
                public BigDecimal toUsd(BigDecimal amount, String currency) {
                    return amount.multiply(new BigDecimal("1.25"));
                }
            });

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.00", "-0.000"})
    void filtersEveryZeroAmountBeforeValidation(String amount) {
        Transaction transaction = new Transaction(1, null, new BigDecimal(amount), "unsupported", TIMESTAMP);

        assertNull(processor.process(transaction));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n", "\u2003"})
    void rejectsNullOrBlankAccountIds(String accountId) {
        Transaction transaction = new Transaction(1, accountId, BigDecimal.ONE, "EUR", TIMESTAMP);

        assertThrows(InvalidTransactionException.class, () -> processor.process(transaction));
    }

    @Test
    void rejectsUnsupportedCurrency() {
        Transaction transaction = new Transaction(1, "account-1", BigDecimal.ONE, "unsupported", TIMESTAMP);

        assertThrows(InvalidTransactionException.class, () -> processor.process(transaction));
    }

    @ParameterizedTest
    @CsvSource({"10.00, 12.5000", "-10.00, -12.5000"})
    void convertsValidTransactionsAndUsesTheTimestampDate(String amount, String expectedUsd) {
        Transaction transaction = new Transaction(42, "account-42", new BigDecimal(amount), "EUR", TIMESTAMP);

        assertEquals(new SettlementLine(42, "account-42", new BigDecimal(expectedUsd),
                LocalDate.of(2026, 10, 26)), processor.process(transaction));
    }

    @Test
    void writesChunksOfFiveHundredItems() throws Exception {
        List<Transaction> transactions = IntStream.rangeClosed(1, 1001)
                .mapToObj(this::validTransaction).toList();
        List<Integer> chunkSizes = new ArrayList<>();

        StepExecution execution = runStep(new ListItemReader<>(transactions),
                chunk -> chunkSizes.add(chunk.size()));

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        assertEquals(List.of(500, 500, 1), chunkSizes);
        assertEquals(1001, execution.getWriteCount());
    }

    @Test
    void filteringDoesNotConsumeTheSkipLimit() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        for (int id = 1; id <= 120; id++) {
            transactions.add(new Transaction(id, "account-1", BigDecimal.ZERO, "EUR", TIMESTAMP));
        }
        transactions.add(validTransaction(121));

        StepExecution execution = runStep(new ListItemReader<>(transactions), chunk -> { });

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        assertEquals(120, execution.getFilterCount());
        assertEquals(0, execution.getSkipCount());
        assertEquals(1, execution.getWriteCount());
    }

    @Test
    void allowsOneHundredSkipsAcrossReadingAndProcessing() throws Exception {
        StepExecution execution = runStep(readerWithMixedErrors(50), chunk -> { });

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        assertEquals(50, execution.getReadSkipCount());
        assertEquals(50, execution.getProcessSkipCount());
        assertEquals(100, execution.getSkipCount());
        assertEquals(1, execution.getWriteCount());
    }

    @Test
    void failsOnTheOneHundredAndFirstSkippableError() throws Exception {
        StepExecution execution = runStep(readerWithMixedErrors(51), chunk -> { });

        assertEquals(BatchStatus.FAILED, execution.getStatus());
        assertTrue(execution.getFailureExceptions().stream()
                .anyMatch(SkipLimitExceededException.class::isInstance));
    }

    @Test
    void failsForAnExceptionThatIsNotSkippable() throws Exception {
        ItemReader<Transaction> reader = () -> {
            throw new IllegalStateException("Reader unavailable");
        };

        StepExecution execution = runStep(reader, chunk -> { });

        assertEquals(BatchStatus.FAILED, execution.getStatus());
        assertEquals(0, execution.getSkipCount());
    }

    private Transaction validTransaction(long id) {
        return new Transaction(id, "account-" + id, BigDecimal.ONE, "EUR", TIMESTAMP);
    }

    private ItemReader<Transaction> readerWithMixedErrors(int invalidTransactionCount) {
        List<Object> records = new ArrayList<>();
        for (int line = 1; line <= 50; line++) {
            records.add(new FlatFileParseException("Malformed transaction", "invalid", line));
        }
        for (int id = 1; id <= invalidTransactionCount; id++) {
            records.add(new Transaction(id, " ", BigDecimal.ONE, "EUR", TIMESTAMP));
        }
        records.add(validTransaction(1000));
        Iterator<Object> iterator = records.iterator();
        return () -> {
            if (!iterator.hasNext()) {
                return null;
            }
            Object record = iterator.next();
            if (record instanceof FlatFileParseException exception) {
                throw exception;
            }
            return (Transaction) record;
        };
    }

    private StepExecution runStep(ItemReader<Transaction> reader, ItemWriter<SettlementLine> writer)
            throws Exception {
        ResourcelessJobRepository repository = new ResourcelessJobRepository();
        Step step = new SettlementStepConfig().settlementStep(repository,
                new ResourcelessTransactionManager(), reader, processor, writer);
        JobExecution jobExecution = repository.createJobExecution("settlementJob", new JobParameters());
        StepExecution execution = new StepExecution(step.getName(), jobExecution, 1L);
        step.execute(execution);
        return execution;
    }
}
