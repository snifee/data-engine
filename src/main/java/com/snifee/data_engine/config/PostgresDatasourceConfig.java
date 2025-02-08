package com.snifee.data_engine.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Component
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.snifee.data_engine.repository",
        entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresDatasourceConfig {

    @Bean(name = "postgresDatasource")
    @ConfigurationProperties("spring.datasource")
    public DataSource postgresDatasource(){
        return new DataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "postgresEntityManager")
    public LocalContainerEntityManagerFactoryBean postgresEntityManager(
            @Qualifier("postgresDatasource") DataSource dataSource,
            EntityManagerFactoryBuilder builder
    ){
        return builder
                .dataSource(dataSource)
                .packages("com.snifee.data_engine.entity")
                .build();
    }

    @Bean("postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManagerFactoryBean")EntityManagerFactory entityManagerFactory
            ){
        return new JpaTransactionManager(entityManagerFactory);
    }

}
