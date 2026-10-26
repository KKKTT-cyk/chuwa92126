package org.tiff.homework11.question11;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SettlementStepConfig {

    @Bean
    public TransactionValidationProcessor transactionValidationProcessor(ExchangeRateService rates) {
        return new TransactionValidationProcessor(rates);
    }

    @Bean
    public Step settlementStep(JobRepository jobRepository,
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
