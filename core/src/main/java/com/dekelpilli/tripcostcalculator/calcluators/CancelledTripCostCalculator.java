package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CancelledTripCostCalculator implements TripCostCalculator {
    private static final BigDecimal TRIP_COST = BigDecimal.ZERO.setScale(2);

    @Override
    public BigDecimal calculateChargeAmount(Tap tapOn, Tap tapOff) {
        return TRIP_COST;
    }

    @Override
    public TripStatus getStatus() {
        return TripStatus.CANCELLED;
    }
}
