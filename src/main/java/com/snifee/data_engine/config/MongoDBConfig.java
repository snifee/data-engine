package com.snifee.data_engine.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(
        basePackages = "com.snifee.data_engine.mongodb.repository"
)
public class MongoDBConfig {

    @Value(value = "${spring.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

}
