package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public final class CancelledTripCostCalculator extends TripCostCalculator {
    private static final BigDecimal TRIP_COST = BigDecimal.ZERO.setScale(2);

    public CancelledTripCostCalculator(TripCostCalculatorConfiguration tripCostCalculatorConfiguration) {
        super(tripCostCalculatorConfiguration);
    }

    @Override
    BigDecimal calculateTripCost(Tap tapOn, Tap tapOff) {
        return TRIP_COST;
    }

    @Override
    public TripStatus getStatus() {
        return TripStatus.CANCELLED;
    }
}
