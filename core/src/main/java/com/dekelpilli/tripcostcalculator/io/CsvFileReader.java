package com.dekelpilli.tripcostcalculator.io;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Iterator;

@Component
@AllArgsConstructor
public class CsvFileReader {

    private final CsvMapper csvMapper;

    @SneakyThrows //TODO: deal with bad csvs
    public <T> Iterator<T> parse(String filename, Class<T> clazz) {

        File inputFile = new File(filename);
        return csvMapper.readerFor(clazz).with(CsvSchema.emptySchema().withHeader()).readValues(inputFile);
    }
}
