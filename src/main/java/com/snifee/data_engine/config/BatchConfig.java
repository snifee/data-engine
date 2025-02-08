package com.snifee.data_engine.config;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
public class BatchConfig {

    @Autowired
    @Qualifier("h2DBdataSource")
    private DataSource h2DBdataSource;

    @Autowired
    @Qualifier("h2TransactionManager")
    private PlatformTransactionManager h2TransactionManager;

    @Bean(name = "jobRepository")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(h2DBdataSource);
        factory.setTransactionManager(h2TransactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }


    public <T> JdbcCursorItemReader<T> reader (
            RowMapper<T> mapper,
            String query,
            DataSource dataSource
    ){
        JdbcCursorItemReader<T> reader = new JdbcCursorItemReader<T>();
        reader.setDataSource(dataSource);
        reader.setSql(query);
        reader.setRowMapper(mapper);
        reader.setFetchSize(10);
        reader.setQueryTimeout(3000);
        reader.setMaxRows(10);
        return reader;
    }

}
