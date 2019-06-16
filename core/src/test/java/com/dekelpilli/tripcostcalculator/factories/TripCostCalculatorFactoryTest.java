package com.dekelpilli.tripcostcalculator.factories;

import com.dekelpilli.tripcostcalculator.calcluators.TripCostCalculator;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TripCostCalculatorFactoryTest {

    @Test
    void givenMultiplesCalculatorsWithTheSameStatus__ThrowsException() {
        TripCostCalculator tripCostCalculator1 = mock(TripCostCalculator.class);
        TripCostCalculator tripCostCalculator2 = mock(TripCostCalculator.class);
        when(tripCostCalculator1.getStatus()).thenReturn(TripStatus.COMPLETED);
        when(tripCostCalculator2.getStatus()).thenReturn(TripStatus.COMPLETED);
        assertThrows(BeanCreationException.class,
                () -> new TripCostCalculatorFactory(List.of(tripCostCalculator1, tripCostCalculator2)));
    }

    @Test
    void givenCalculatorsWithDistinctStatuses__ReturnsAppropriateCalculators() {
        TripCostCalculator completedTripCostCalculator = mock(TripCostCalculator.class);
        TripCostCalculator cancelledTripCostCalculator = mock(TripCostCalculator.class);
        TripCostCalculator incompleteTripCostCalculator = mock(TripCostCalculator.class);
        when(completedTripCostCalculator.getStatus()).thenReturn(TripStatus.COMPLETED);
        when(cancelledTripCostCalculator.getStatus()).thenReturn(TripStatus.CANCELLED);
        when(incompleteTripCostCalculator.getStatus()).thenReturn(TripStatus.INCOMPLETE);
        TripCostCalculatorFactory tripCostCalculatorFactory = new TripCostCalculatorFactory(
                List.of(completedTripCostCalculator, cancelledTripCostCalculator, incompleteTripCostCalculator));
        assertSame(completedTripCostCalculator, tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.COMPLETED));
        assertSame(cancelledTripCostCalculator, tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.CANCELLED));
        assertSame(incompleteTripCostCalculator, tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.INCOMPLETE));
    }
}
