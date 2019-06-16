package com.dekelpilli.tripcostcalculator.configurations;


import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;


@Configuration
public class JacksonConfiguration {

    @Bean
    public CsvMapper csvMapper(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(CsvParser.Feature.TRIM_SPACES, true);
        jacksonObjectMapperBuilder.configure(csvMapper);
        return csvMapper;
    }
}
