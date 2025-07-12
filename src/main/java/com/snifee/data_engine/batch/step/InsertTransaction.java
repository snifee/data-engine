package com.snifee.data_engine.batch.step;

import com.snifee.data_engine.dto.TransactionCSVDto;
import com.snifee.data_engine.postresql.entity.Transaction;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.StoredProcedureItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
public class InsertTransaction {

    @Autowired
    @Qualifier("postgresDatasource")
    private DataSource postgresDatasource;

    @Bean
    @StepScope
    public ItemReader<TransactionCSVDto> itemReader(){
        FileSystemResource resource = new FileSystemResource("transaction.csv");
        return new FlatFileItemReaderBuilder<TransactionCSVDto>()
                .resource(resource)
                .fieldSetMapper(mapper())
                .build();
    }


    @Bean
    @StepScope
    private LineMapper<TransactionCSVDto> mapper(){
        DefaultLineMapper<TransactionCSVDto> lineMapper = new DefaultLineMapper<>();

        return Mapper
    }

}
