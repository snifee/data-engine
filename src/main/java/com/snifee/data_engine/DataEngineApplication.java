package com.snifee.data_engine;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class DataEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataEngineApplication.class, args);
	}

}
