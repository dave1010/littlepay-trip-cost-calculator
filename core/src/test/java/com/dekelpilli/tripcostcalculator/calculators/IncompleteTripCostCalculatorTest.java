package com.dekelpilli.tripcostcalculator.calculators;

import com.dekelpilli.tripcostcalculator.calcluators.IncompleteTripCostCalculator;
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


class IncompleteTripCostCalculatorTest {

    private TripCostCalculatorConfiguration tripCostCalculatorConfiguration;

    private IncompleteTripCostCalculator incompleteTripCostCalculator;

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
    void givenMultipleStopMappingConfigurations__findsMaximumCostForGivenStations() {
        String testStopId = RandomStringUtils.randomAlphanumeric(10);
        String testStopId2 = RandomStringUtils.randomAlphanumeric(8);
        BigDecimal stop2MaximumCost = BigDecimal.valueOf(5L);
        tripCostCalculatorConfiguration.setCostMappings(List.of(
                createCostMapping(BigDecimal.ONE, testStopId, RandomStringUtils.randomAlphanumeric(9)),
                createCostMapping(BigDecimal.ZERO, testStopId, testStopId2),
                createCostMapping(stop2MaximumCost, RandomStringUtils.randomAlphanumeric(6), testStopId2),
                createCostMapping(BigDecimal.TEN, testStopId, RandomStringUtils.randomAlphanumeric(7))
        ));

        incompleteTripCostCalculator = new IncompleteTripCostCalculator(tripCostCalculatorConfiguration);
        assertEquals(BigDecimal.TEN, calculateChargeAmount(testStopId));
        assertEquals(stop2MaximumCost, calculateChargeAmount(testStopId2));
    }

    @Test
    void usedForIncompleteTrips() {
        tripCostCalculatorConfiguration.setCostMappings(Collections.emptyList());
        incompleteTripCostCalculator = new IncompleteTripCostCalculator(tripCostCalculatorConfiguration);
        assertEquals(TripStatus.INCOMPLETE, incompleteTripCostCalculator.getStatus());
    }

    private BigDecimal calculateChargeAmount(String stopId) {
        Tap tapOn = mock(Tap.class);
        when(tapOn.getStopId()).thenReturn(stopId);
        return incompleteTripCostCalculator.calculateChargeAmount(tapOn, null);
    }
}
