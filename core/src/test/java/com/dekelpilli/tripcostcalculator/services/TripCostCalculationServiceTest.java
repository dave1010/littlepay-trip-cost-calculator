package com.dekelpilli.tripcostcalculator.services;


import com.dekelpilli.tripcostcalculator.calcluators.TripCostCalculator;
import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.factories.TripCostCalculatorFactory;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TapType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TripCostCalculationServiceTest {

    private static final String INPUT = RandomStringUtils.randomAlphabetic(10);
    private static final String CURRENCY = RandomStringUtils.random(1);

    @Mock
    private CsvFileReader csvFileReader;

    @Mock
    private TripCostCalculatorFactory tripCostCalculatorFactory;

    @Mock
    private TripCostCalculatorConfiguration tripCostCalculatorConfiguration;

    @Mock
    private TripCostCalculator tripCostCalculator;

    private TripCostCalculationService tripCostCalculationService;

    @BeforeEach
    void setup() {
        when(tripCostCalculatorConfiguration.getInput()).thenReturn(INPUT);
        when(tripCostCalculatorConfiguration.getCurrencySymbol()).thenReturn(CURRENCY);

        tripCostCalculationService = new TripCostCalculationService(tripCostCalculatorConfiguration,
                tripCostCalculatorFactory, csvFileReader);
    }

    @Test
    @Disabled
    void givenDataForSingleCompleteTrip__GetsCompletedTripCostForGivenStopsAndWitesToFile() {

        String stop1 = RandomStringUtils.randomAlphabetic(10);
        String stop2 = RandomStringUtils.randomAlphabetic(9);
        String companyId = RandomStringUtils.randomAlphabetic(8);
        String busId = RandomStringUtils.randomAlphabetic(7);
        String pan = RandomStringUtils.randomAlphabetic(6);

        Tap tapOn = new Tap(nextInt(), new Date(), TapType.ON, stop1, companyId, busId, pan);
        Tap tapOff = new Tap(nextInt(), Date.from(Instant.now().plusSeconds(10)), TapType.OFF, stop2, companyId, busId, pan);

        when(csvFileReader.parse(INPUT, Tap.class))
                .thenReturn(List.of(tapOn, tapOff).iterator());

        tripCostCalculationService.calculateTripCosts();
    }
}
