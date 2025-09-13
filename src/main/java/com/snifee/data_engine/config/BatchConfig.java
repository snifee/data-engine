package com.snifee.data_engine.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableBatchProcessing(
        dataSourceRef = "h2Datasource",
        transactionManagerRef = "h2TransactionManager"
)
@Configuration
public class BatchConfig {

    @Autowired
    @Qualifier("h2Datasource")
    private DataSource h2DBdataSource;

    @Autowired
    @Qualifier("h2TransactionManager")
    private PlatformTransactionManager h2TransactionManager;


    @Bean(name = "jobRepository")
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(h2DBdataSource);
        factory.setTransactionManager(h2TransactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher jobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
