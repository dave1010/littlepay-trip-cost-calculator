package com.dekelpilli.tripcostcalculator.configurations;


import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public CsvMapper csvMapper() {
        CsvMapper csvMapper = new CsvMapper();
        //TODO: dateformat, features
        return csvMapper;
    }
}
