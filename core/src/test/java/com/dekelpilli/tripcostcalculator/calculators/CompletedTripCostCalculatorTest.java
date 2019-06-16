package com.dekelpilli.tripcostcalculator.calculators;


import com.dekelpilli.tripcostcalculator.calcluators.CompletedTripCostCalculator;
import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CompletedTripCostCalculatorTest {

    private CompletedTripCostCalculator completedTripCostCalculator;
    private TripCostCalculatorConfiguration tripCostCalculatorConfiguration;

    private static TripCostCalculatorConfiguration.CostMapping createCostMapping(BigDecimal cost,
                                                                                 String stop1, String stop2) {

        TripCostCalculatorConfiguration.CostMapping costMapping = new TripCostCalculatorConfiguration.CostMapping();
        costMapping.setCost(cost);
        costMapping.setStops(List.of(stop1, stop2));
        return costMapping;
    }

    @BeforeEach
    void setup() {
        tripCostCalculatorConfiguration = new TripCostCalculatorConfiguration();
    }

    @Test
    void givenMultipleMappingsForTheSameStop__CalculatesCorrectCostForGivenStopsRegardlessOfOrder() {
        String testStopId1 = RandomStringUtils.randomAlphanumeric(10);
        String testStopId2 = RandomStringUtils.randomAlphanumeric(9);
        tripCostCalculatorConfiguration.setCostMappings(List.of(
                createCostMapping(BigDecimal.ZERO, testStopId1, RandomStringUtils.randomAlphanumeric(8)),
                createCostMapping(BigDecimal.ONE, testStopId1, testStopId2),
                createCostMapping(BigDecimal.TEN, testStopId2, RandomStringUtils.randomAlphanumeric(7)))
        );
        completedTripCostCalculator = new CompletedTripCostCalculator(tripCostCalculatorConfiguration);
        assertEquals(BigDecimal.ONE, calculateChargeAmount(testStopId1, testStopId2));
        assertEquals(BigDecimal.ONE, calculateChargeAmount(testStopId2, testStopId1));

        //reverse input order
        tripCostCalculatorConfiguration.setCostMappings(List.of(
                createCostMapping(BigDecimal.ONE, testStopId2, RandomStringUtils.randomAlphanumeric(7)),
                createCostMapping(BigDecimal.ZERO, testStopId1, RandomStringUtils.randomAlphanumeric(8)),
                createCostMapping(BigDecimal.TEN, testStopId2, testStopId1))
        );
        completedTripCostCalculator = new CompletedTripCostCalculator(tripCostCalculatorConfiguration);
        assertEquals(BigDecimal.TEN, calculateChargeAmount(testStopId1, testStopId2));
        assertEquals(BigDecimal.TEN, calculateChargeAmount(testStopId2, testStopId1));
    }

    private BigDecimal calculateChargeAmount(String stopId1, String stopId2) {
        Tap tapOn = mock(Tap.class);
        when(tapOn.getStopId()).thenReturn(stopId1);
        Tap tapOff = mock(Tap.class);
        when(tapOff.getStopId()).thenReturn(stopId2);
        return completedTripCostCalculator.calculateChargeAmount(tapOn, tapOff);
    }

    @Test
    void usedForCompleteTrips() {
        tripCostCalculatorConfiguration.setCostMappings(Collections.emptyList());
        completedTripCostCalculator = new CompletedTripCostCalculator(tripCostCalculatorConfiguration);
        assertEquals(TripStatus.COMPLETED, completedTripCostCalculator.getStatus());
    }
}
