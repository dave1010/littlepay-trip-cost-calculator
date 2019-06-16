package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
public class IncompleteTripCostCalculator implements TripCostCalculator {

    private final Map<String, BigDecimal> stopIdMaximumCostMap;

    public IncompleteTripCostCalculator(TripCostCalculatorConfiguration tripCostCalculatorConfiguration) {
        stopIdMaximumCostMap = createMaximumCostsMap(tripCostCalculatorConfiguration.getCostMappings());
    }

    private static Map<String, BigDecimal> createMaximumCostsMap(
            Collection<TripCostCalculatorConfiguration.CostMapping> costMappings) {

        HashMap<String, BigDecimal> maximumCostsMap = new HashMap<>();
        costMappings.forEach(costMapping -> {
            BigDecimal cost = costMapping.getCost();
            costMapping.getStops().forEach(stopId -> {
                if (maximumCostsMap.containsKey(stopId)) {
                    maximumCostsMap.put(stopId, maximumCostsMap.get(stopId).max(cost));
                } else {
                    maximumCostsMap.put(stopId, cost);
                }
            });
        });
        return maximumCostsMap;
    }

    @Override
    public BigDecimal calculateChargeAmount(Tap tapOn, Tap tapOff) {
        return stopIdMaximumCostMap.get(tapOn.getStopId());
    }

    @Override
    public TripStatus getStatus() {
        return TripStatus.INCOMPLETE;
    }
}
