package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;

import java.math.BigDecimal;


public interface TripCostCalculator {

    BigDecimal calculateChargeAmount(Tap tapOn, Tap tapOff);

    TripStatus getStatus();
}
