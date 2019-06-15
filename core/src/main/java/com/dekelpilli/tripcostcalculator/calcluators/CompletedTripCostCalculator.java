package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.dto.UnorderedPair;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Component
public final class CompletedTripCostCalculator extends TripCostCalculator {

    private Map<UnorderedPair<String>, BigDecimal> stopPairCostMap;

    public CompletedTripCostCalculator(TripCostCalculatorConfiguration tripCostCalculatorConfiguration) {

        stopPairCostMap = new HashMap<>();
        tripCostCalculatorConfiguration.getCostMappings().forEach(costMapping -> {
                    UnorderedPair<String> stopPair = UnorderedPair.fromList(costMapping.getStops());
                    stopPairCostMap.put(stopPair, costMapping.getCost());
        });
    }

    @Override
    public BigDecimal calculateChargeAmount(Tap tapOn, Tap tapOff) {
        return stopPairCostMap.get(new UnorderedPair<>(tapOn.getStopId(), tapOff.getStopId()));
    }

    @Override
    public TripStatus getStatus() {
        return TripStatus.COMPLETED;
    }
}
