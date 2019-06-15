package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;

import java.math.BigDecimal;

public abstract class TripCostCalculator {

    public abstract BigDecimal calculateChargeAmount(Tap tapOn, Tap tapOff);

    public abstract TripStatus getStatus();
}
