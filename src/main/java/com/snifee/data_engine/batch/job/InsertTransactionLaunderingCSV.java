package com.snifee.data_engine.batch.job;

import com.snifee.data_engine.dto.TransactionCSVDto;
import com.snifee.data_engine.postresql.entity.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Configuration
public class InsertTransactionLaunderingCSV {

    @Autowired
    @Qualifier("postgresDatasource")
    private DataSource postgresDatasource;

    @Value("${nio.path-to-dir}")
    private String path;

    @Value("${nio.filename}")
    private String filename;

    @Bean(name = "jobInsertTransactionLaunderingCSV")
    public Job jobInsertTransactionLaunderingCSV(
            @Qualifier("postgresTransactionManager") PlatformTransactionManager platformTransactionManager,
            @Qualifier("jobRepository") JobRepository jobRepository
    ){
        return new JobBuilder("jobInsertTransactionLaunderingCSV", jobRepository)
                .start(stepInsertTransactionLaundering(platformTransactionManager,jobRepository))
                .build();
    }

    @Bean(name = "stepInsertTransactionLaundering")
    public Step stepInsertTransactionLaundering(
            @Qualifier("postgresTransactionManager") PlatformTransactionManager platformTransactionManager,
            @Qualifier("jobRepository") JobRepository jobRepository
    ){
        return new StepBuilder("stepInsertTransactionLaundering",jobRepository)
                .<TransactionCSVDto, Transaction>chunk(1000,platformTransactionManager)
                .reader(itemReader())
                .processor(processor())
                .writer(writer())
                .build();
    }


    @StepScope
    private ItemReader<TransactionCSVDto> itemReader() throws UnexpectedInputException, ParseException {
        String[] tokens = {
                "Timestamp",
                "From Bank",
                "Account",
                "To Bank",
                "Account",
                "Amount Received",
                "Receiving Currency",
                "Amount Paid",
                "Payment Currency",
                "Payment Format",
                "Is Laundering"
        };

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(tokens);

        Resource resource = new FileSystemResource(path+"/"+filename);

        return new FlatFileItemReaderBuilder<TransactionCSVDto>()
                .resource(resource)
                .name("transactionCSVReader")
                .linesToSkip(1)
                .lineMapper(lineMapper(tokenizer))
                .build();
    }

    private static DefaultLineMapper<TransactionCSVDto> lineMapper(DelimitedLineTokenizer tokenizer) {
        DefaultLineMapper<TransactionCSVDto> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSet -> {
            return new TransactionCSVDto(
                    fieldSet.readString("Timestamp"),
                    fieldSet.readString("From Bank"),
                    fieldSet.readString("Account"),
                    fieldSet.readString("To Bank"),
                    fieldSet.readString("Account"),
                    fieldSet.readString("Amount Received"),
                    fieldSet.readString("Receiving Currency"),
                    fieldSet.readString("Amount Paid"),
                    fieldSet.readString("Payment Currency"),
                    fieldSet.readString("Payment Format"),
                    fieldSet.readBoolean("Is Laundering")
            );
        });
        return lineMapper;
    }

    @StepScope
    private ItemProcessor<TransactionCSVDto, Transaction> processor(){

        return (TransactionCSVDto item) -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

            // Parse the string into a LocalDate object
            LocalDateTime localDate = LocalDateTime.parse(item.timestamp(), formatter);

            Transaction transaction = new Transaction();
            transaction.setId(UUID.randomUUID());
            transaction.setDatetime(Timestamp.valueOf(localDate));
            transaction.setSourceBank(item.fromBank());
            transaction.setSenderAccount(item.account1());
            transaction.setDestinationBank(item.toBank());
            transaction.setReceiverAccount(item.account2());
            transaction.setAmountPaid(Double.parseDouble(item.amountPaid()));
            transaction.setPaymentCurrency(item.paymentCurrency());
            transaction.setAmountReceived(Double.parseDouble(item.amountReceived()));
            transaction.setPaymentCurrency(item.paymentCurrency());
            transaction.setPaymentFormat(item.paymentFormat());
            transaction.setReceivingCurrency(item.receivingCurrency());
            transaction.setLaundering(item.isLaundering());
            return transaction;
        };
    }


    @Bean
    @StepScope
    public JdbcBatchItemWriter<Transaction> writer(){
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(postgresDatasource)
                .sql("""
                    INSERT INTO transaction_laundering(
                        id,
                        datetime,
                        source_bank,
                        sender_account,
                        destination_bank,
                        receiver_account,
                        amount_received,
                        receiving_currency,
                        amount_paid,
                        payment_currency,
                        payment_format,
                        is_laundering
                    ) VALUES (
                        :id,
                        :datetime,
                        :sourceBank,
                        :senderAccount,
                        :destinationBank,
                        :receiverAccount,
                        :amountReceived,
                        :receivingCurrency,
                        :amountPaid,
                        :paymentCurrency,
                        :paymentFormat,
                        :laundering
                    )
                """
                )
                .beanMapped()
                .build();
    }

}
