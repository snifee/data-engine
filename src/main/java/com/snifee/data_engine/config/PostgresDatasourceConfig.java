package com.snifee.data_engine.config;

import com.snifee.data_engine.postresql.entity.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
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


    @Value(value = "${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    @Bean(name = "postgresDatasource")
    public DataSource postgresDatasource()  {
        return DataSourceBuilder
                .create()
                .url(jdbcUrl)
                .build();
    }

    @Bean(name = "postgresEntityManager")
    public LocalContainerEntityManagerFactoryBean postgresEntityManager() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.snifee.data_engine.postresql.entity");
        factory.setDataSource(postgresDatasource());
        return factory;
    }

    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(postgresEntityManager().getObject());
        return txManager;
    }

}
