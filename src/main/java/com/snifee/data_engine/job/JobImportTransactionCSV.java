package com.snifee.data_engine.job;

import com.snifee.data_engine.postresql.entity.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.core.io.Resource;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
public class JobImportTransactionCSV {

    @Autowired
    @Qualifier("jobRepository")
    private JobRepository jobRepository;

    @Autowired
    @Qualifier("postgresDatasource")
    private DataSource postgresDatasource;

    @Autowired
    @Qualifier("postgresEntityManager")
    private EntityManagerFactory postgresEntityManager;

    @Autowired
    @Qualifier("postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager;

    @Value("")
    private Resource resource;

    @Bean
    public Job importTransactionCSVJob(){
        return new JobBuilder("importTransactionCSVJob", jobRepository)
                .start(importTransactionCSVStep())
                .build();
    }

    @Bean
    public Step importTransactionCSVStep(){
        return new StepBuilder("importTransactionCSV", jobRepository)
                .<Transaction, Transaction>chunk(10, postgresTransactionManager)
                .reader(flatFileItemReader())
                .processor(processorTransactionCSV())
                .writer(jpaItemWriterTransaction())
                .build();
    }

    @Bean
    public ItemProcessor<Transaction, Transaction> processorTransactionCSV(){
        return item -> item;
    }

    @Bean
    public FlatFileItemReader<Transaction> flatFileItemReader(){
        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .delimited()
                .names("Timestamp","From Bank","Account","To Bank","Account","Amount Received","Receiving Currency",
                        "Amount Paid","Payment Currency","Payment Format","Is Laundering")
                .resource(resource)
                .targetType(Transaction.class)
                .fieldSetMapper(CSVtoTransaction())
                .build();
    }

    @Bean
    public FieldSetMapper<Transaction> CSVtoTransaction(){
        return fieldSet -> {
            Transaction transaction = new Transaction();
            transaction.setId(UUID.randomUUID());
            transaction.setPaymentFormat(fieldSet.readString("Payment Format"));
            transaction.setSourceBank(fieldSet.readString("From Bank"));
            transaction.setAmount(fieldSet.readDouble("Amount Paid"));
            transaction.setPaymentCurrency(fieldSet.readString("Payment Currency"));
            transaction.setDestBank(fieldSet.readString("To Bank"));
            transaction.setReceivingCurrency(fieldSet.readString("Receiving Currency"));
            transaction.setLaundering(fieldSet.readBoolean("Is Laundering"));
            transaction.setAccount(fieldSet.readString("Account"));
            return transaction;
        };
    }

    @Bean
    public JpaItemWriter<Transaction> jpaItemWriterTransaction(){
        return new JpaItemWriterBuilder<Transaction>()
                .entityManagerFactory(postgresEntityManager)
                .build();
    }

}
