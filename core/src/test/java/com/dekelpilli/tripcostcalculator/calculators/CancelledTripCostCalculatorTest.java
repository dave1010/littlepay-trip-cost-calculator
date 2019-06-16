package com.dekelpilli.tripcostcalculator.calculators;


import com.dekelpilli.tripcostcalculator.calcluators.CancelledTripCostCalculator;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CancelledTripCostCalculatorTest {
    private CancelledTripCostCalculator cancelledTripCostCalculator;

    @BeforeEach
    void setup() {
        cancelledTripCostCalculator = new CancelledTripCostCalculator();
    }

    @Test
    void forAnyInput__CostIsZero() {
        assertEquals(BigDecimal.ZERO.setScale(2), cancelledTripCostCalculator.calculateChargeAmount(null, null));
    }

    @Test
    void usedForCancelledTrips() {
        assertEquals(TripStatus.CANCELLED, cancelledTripCostCalculator.getStatus());
    }
}
