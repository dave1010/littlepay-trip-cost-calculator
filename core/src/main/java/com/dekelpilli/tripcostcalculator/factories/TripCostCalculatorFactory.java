package com.dekelpilli.tripcostcalculator.factories;

import com.dekelpilli.tripcostcalculator.calcluators.TripCostCalculator;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
public class TripCostCalculatorFactory {

    private final Map<TripStatus, TripCostCalculator> tripCostCalculatorMap;

    public TripCostCalculatorFactory(List<TripCostCalculator> tripCostCalculators) {
        tripCostCalculatorMap = tripCostCalculators
                .stream()
                .collect(Collectors.toMap(TripCostCalculator::getStatus, UnaryOperator.identity(),
                        (oldValue, newValue) -> {
                            throw new BeanCreationException("Multiple calculators found for " + oldValue.getStatus());
                        }));
    }

    public TripCostCalculator getCalculatorForStatus(TripStatus tripStatus) {
        return tripCostCalculatorMap.get(tripStatus);
    }
}
