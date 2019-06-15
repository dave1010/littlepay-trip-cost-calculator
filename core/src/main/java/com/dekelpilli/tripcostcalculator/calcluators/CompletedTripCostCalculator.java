package com.dekelpilli.tripcostcalculator.calcluators;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public final class CompletedTripCostCalculator extends TripCostCalculator {

    private Map<StopPair, BigDecimal> stopPairCostMap;

    public CompletedTripCostCalculator(TripCostCalculatorConfiguration tripCostCalculatorConfiguration) {
        super(tripCostCalculatorConfiguration);

        stopPairCostMap = new HashMap<>();
        tripCostCalculatorConfiguration.getCostMappings().forEach(costMapping -> {
                    StopPair stopPair = StopPair.fromList(costMapping.getStops());
                    stopPairCostMap.put(stopPair, costMapping.getCost());
        });
    }

    @Override
    public TripStatus getStatus() {
        return TripStatus.COMPLETED;
    }

    @Override
    BigDecimal calculateTripCost(Tap tapOn, Tap tapOff) {
        return stopPairCostMap.get(new StopPair(tapOn.getStopId(), tapOff.getStopId()));
    }

    //TODO: make generic, this has nothing to do with stops
    private static class StopPair {
        private final String stop1;
        private final String stop2;

        private StopPair(String stop1, String stop2) {
            this.stop1 = stop1;
            this.stop2 = stop2;

            if (stop1.equals(stop2)) {
                throw new IllegalArgumentException("Provided stops must be different");
            }
        }

        private static StopPair fromList(List<String> stops) {
            if (stops.size() != 2) {
                throw new IllegalArgumentException("List must have exactly 2 stops");
            }
            return new StopPair(stops.get(0), stops.get(1));
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof StopPair)) {
                return false;
            }
            return equalsIgnoreOrder((StopPair) other);
        }

        @Override
        public int hashCode() {
            return stop1.hashCode() ^ stop2.hashCode();
        }

        private boolean equalsIgnoreOrder(StopPair other) {
            return this.stop1.equals(other.stop1) && this.stop2.equals(other.stop2)
                    || this.stop1.equals(other.stop2) && this.stop2.equals(other.stop1);
        }
    }
}
