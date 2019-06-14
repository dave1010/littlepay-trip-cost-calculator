package com.dekelpilli.tripcostcalculator.services;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import org.springframework.stereotype.Service;

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
        csvFileReader.parse(inputFileName, Class.class);
    }
}
