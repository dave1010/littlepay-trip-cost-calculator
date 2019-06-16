package com.dekelpilli.tripcostcalculator.io;

import ch.qos.logback.core.util.FileSize;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
@Component
@AllArgsConstructor
public class CsvFileWriter {
    private final CsvMapper csvMapper;

    public <T> void createFile(String filename, List<T> data, Class<T> clazz) throws IOException {
        File outputFile = new File(filename);
        outputFile.getParentFile().mkdirs();
        FileOutputStream tempFileOutputStream = new FileOutputStream(outputFile);
        BufferedOutputStream bufferedOutputStream
                = new BufferedOutputStream(tempFileOutputStream, (int) FileSize.MB_COEFFICIENT);
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8);

        CsvSchema clazzSchema = csvMapper.schemaFor(clazz).withHeader().withoutQuoteChar();
        ObjectWriter clazzObjectWriter = csvMapper.writer(clazzSchema);
        clazzObjectWriter.writeValue(writerOutputStream, data);
        log.info("Successfully written {} items to {}", data.size(), filename);
    }
}
