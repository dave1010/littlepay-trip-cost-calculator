package com.dekelpilli.tripcostcalculator.io;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class CsvFileWriter {
    private final CsvMapper csvMapper;

    public <T> void createFile(String filename, List<T> data) {

    }
}
