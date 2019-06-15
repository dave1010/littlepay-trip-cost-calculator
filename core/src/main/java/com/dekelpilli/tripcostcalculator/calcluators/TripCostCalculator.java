package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import com.dekelpilli.tripcostcalculator.model.Tap;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class TripCostCalculator {

    private final String currencySymbol;

    public TripCostCalculator(TripCostCalculatorConfiguration tripCostCalculatorConfiguration) {
        currencySymbol = tripCostCalculatorConfiguration.getCurrencySymbol();
    }

    public String calculateChargeAmount(Tap tapOn, Tap tapOff) {
        BigDecimal cost = calculateTripCost(tapOn, tapOff);
        return currencySymbol + cost.setScale(2, RoundingMode.HALF_UP);
    }

    public abstract TripStatus getStatus();

    abstract BigDecimal calculateTripCost(Tap tapOn, Tap tapOff);
}
