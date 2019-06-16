package com.dekelpilli.tripcostcalculator.io;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Component
@AllArgsConstructor
public class CsvFileReader {

    private final CsvMapper csvMapper;

    public <T> Iterator<T> parse(String filename, Class<T> clazz) throws IOException {

        File inputFile = new File(filename);
        return csvMapper.readerFor(clazz).with(CsvSchema.emptySchema().withHeader()).readValues(inputFile);
    }
}
