package com.snifee.data_engine.batch.job;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JobExportTransactionAvro {

//    @Autowired
//    @Qualifier("jobRepository")
//    private JobRepository jobRepository;
//
//    @Autowired
//    @Qualifier("postgresDatasource")
//    private DataSource dataSource;
//
//    @Autowired
//    @Qualifier("postgresTransactionManager")
//    private PlatformTransactionManager platformTransactionManager;
//
//    @Bean(name = "jobExportTransaction")
//    public Job jobExportTransaction (){
//        return new JobBuilder("JobExportTransaction", jobRepository)
//                .start(stepExportTransaction())
//                .build();
//    }
//
//    @Bean(name = "stepExportTransaction")
//    public Step stepExportTransaction(){
//        return new StepBuilder("stepExportTransaction", jobRepository)
//                .<Transaction, TRANSACTION>chunk(10,platformTransactionManager)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .build();
//    }
//
//
//    @Bean(name="transactionLaunderingReader")
//    @StepScope
//    public JdbcCursorItemReader<Transaction> reader (){
//        return new JdbcCursorItemReaderBuilder<Transaction>()
//                .dataSource(dataSource)
//                .sql("select * from transaction_laundering")
//                .rowMapper(mapper())
//                .fetchSize(10)
//                .saveState(false)
//                .queryTimeout(3000)
//                .maxRows(10)
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<Transaction, TRANSACTION> processor(){
//        return item -> {
//            TRANSACTION result = new TRANSACTION();
//            result.put("id", item.getId().toString());
//            result.put("source_bank", item.getSourceBank());
//            result.put("dest_bank", item.getDestBank());
//            result.put("account", item.getAccount());
//            result.put("receiving_currency", item.getReceivingCurrency());
//            result.put("amount", item.getAmount());
//            result.put("payment_currency", item.getPaymentCurrency());
//            result.put("payment_format", item.getPaymentFormat());
//            result.put("is_laundering", item.isLaundering()?1:0);
//
//            return result;
//        };
//    }
//
//
//    @Bean
//    @StepScope
//    public AvroItemWriter<TRANSACTION> writer(){
//        FileSystemResource resource = new FileSystemResource("/");
//
//        return new AvroItemWriterBuilder<TRANSACTION>()
//                .resource((WritableResource) resource)
//                .schema(TRANSACTION.getClassSchema().toString())
//                .type(TRANSACTION.class)
//                .build();
//    }

//    @Bean
//    public RowMapper<Transaction> mapper(){
//        return (rs, rowNum) -> {
//            Transaction result = new Transaction();
//            result.setId(UUID.randomUUID());
//            result.setSourceBank(rs.getString("source_bank"));
//            result.setDestBank(rs.getString("dest_bank"));
//            result.setAccount(rs.getString("account"));
//            result.setReceivingCurrency(rs.getString("receiving_currency"));
//            result.setAmount(rs.getDouble("amount"));
//            result.setPaymentCurrency(rs.getString("payment_currency"));
//            result.setPaymentFormat(rs.getString("payment_format"));
//            result.setLaundering(rs.getBoolean("is_laundering"));
//            return result;
//        };
//    }

}
