package com.dekelpilli.tripcostcalculator.io;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Component
public class CsvFileReader {

    private final CsvMapper csvMapper;

    private Map<Class, ObjectReader> objectReaderCache;

    public CsvFileReader(CsvMapper csvMapper) {
        this.csvMapper = csvMapper;

        this.objectReaderCache = new HashMap<>();
    }

    @SneakyThrows //TODO: deal with bad csvs
    public <T> Iterator<T> parse(String filename, Class<T> clazz) {

        File inputFile = new File(filename);
        return getObjectReaderFor(clazz).readValues(inputFile);
    }

    private ObjectReader getObjectReaderFor(Class clazz) {
        if (objectReaderCache.containsKey(clazz)) {
            return objectReaderCache.get(clazz);
        }

        ObjectReader objectReader = csvMapper.readerWithTypedSchemaFor(clazz);
        objectReaderCache.put(clazz, objectReader);
        return objectReader;
    }
}
