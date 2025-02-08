package com.snifee.data_engine.config;


import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class H2DatasourceConfig {

    @Bean("h2DBdataSource")
    @BatchDataSource
    @ConfigurationProperties("spring.h2.datasource")
    public DataSource h2DBdataSource(){
        return new DataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager h2TransactionManager() {
        return new ResourcelessTransactionManager();
    }
}
