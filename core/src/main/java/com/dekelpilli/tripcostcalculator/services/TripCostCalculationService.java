package com.dekelpilli.tripcostcalculator.services;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import com.dekelpilli.tripcostcalculator.model.Tap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class TripCostCalculationService {

    private final String inputFileName;
    private final CsvFileReader csvFileReader;

    public TripCostCalculationService(TripCostCalculatorConfiguration tripCostCalculatorConfiguration,
                                      CsvFileReader csvFileReader) {
        inputFileName = tripCostCalculatorConfiguration.getInput();

        this.csvFileReader = csvFileReader;
    }

    public void calculateTripCosts() {
        Iterator<Tap> tapIterator = csvFileReader.parse(inputFileName, Tap.class);

        System.out.println();
    }
}
