package com.dekelpilli.tripcostcalculator.configurations;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class JacksonConfigurationTest {

    private JacksonConfiguration jacksonConfiguration = new JacksonConfiguration();

    @Test
    void defaultObjectMapperConfigurationsApplyToCsvMapper() {
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = mock(Jackson2ObjectMapperBuilder.class);
        CsvMapper csvMapper = jacksonConfiguration.csvMapper(jackson2ObjectMapperBuilder);
        verify(jackson2ObjectMapperBuilder).configure(same(csvMapper));
    }
}
