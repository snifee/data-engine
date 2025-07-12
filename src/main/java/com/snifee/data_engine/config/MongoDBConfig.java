package com.snifee.data_engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "com.snifee.data_engine.mongodb.repository"
)
public class MongoDBConfig {
    @Bean
    private MongoTemplate mongoTemplate(){
        return new MongoTemplate();
    }
}
