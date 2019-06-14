package com.dekelpilli.tripcostcalculator.io;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@AllArgsConstructor
public class CsvFileReader {

    private final CsvMapper csvMapper;

    public <T> Collection<T> parse(String filename, Class<T> clazz) {
//        csvMapper.readerWithSchemaFor(clazz).readValues(new File());
        return Collections.emptyList();
    }
}
