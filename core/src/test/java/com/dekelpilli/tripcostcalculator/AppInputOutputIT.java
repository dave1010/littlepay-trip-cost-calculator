package com.dekelpilli.tripcostcalculator;


import com.dekelpilli.tripcostcalculator.calcluators.CancelledTripCostCalculator;
import com.dekelpilli.tripcostcalculator.calcluators.CompletedTripCostCalculator;
import com.dekelpilli.tripcostcalculator.calcluators.IncompleteTripCostCalculator;
import com.dekelpilli.tripcostcalculator.configurations.JacksonConfiguration;
import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.factories.TripCostCalculatorFactory;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import com.dekelpilli.tripcostcalculator.io.CsvFileWriter;
import com.dekelpilli.tripcostcalculator.model.Trip;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import com.dekelpilli.tripcostcalculator.services.TripCostCalculationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@EnableAutoConfiguration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(classes = {TripCostCalculationService.class, CsvFileWriter.class, CsvFileReader.class,
        TripCostCalculatorConfiguration.class, CancelledTripCostCalculator.class, IncompleteTripCostCalculator.class,
        CompletedTripCostCalculator.class, TripCostCalculatorFactory.class, JacksonConfiguration.class})
class AppInputOutputIT {

    private final CsvFileReader csvFileReader;
    private final TripCostCalculatorConfiguration tripCostCalculatorConfiguration;
    private final TripCostCalculationService tripCostCalculationService;

    private static boolean firstTime = true;

    private static List<Trip> writtenTrips;

    /**
     * This setup will run the app against a test file then load the output.
     * The tests in this class will make assertions against this loaded data.
     */
    @BeforeEach
    void setup() throws IOException {
        if (firstTime) {
            tripCostCalculationService.calculateTripCosts();
            writtenTrips = new ArrayList<>();

            csvFileReader.parse(tripCostCalculatorConfiguration.getOutput(),
                    Trip.class).forEachRemaining(trip -> writtenTrips.add(trip));
            firstTime = false;
        }
    }

    @Test
    void fromGivenTaps__CreatedFourTripsForTwoUniqueUsers() {
        assertEquals(4, writtenTrips.size());
        assertEquals(2, writtenTrips.stream().map(Trip::getPrimaryAccountNumber).distinct().count());
    }

    @Test
    void fromGivenTaps__FirstUserHasCompleteTripAndIncompleteTrip() {
        List<Trip> firstUserTrips = writtenTrips.stream()
                .filter(trip -> trip.getPrimaryAccountNumber().equals("122000000000003"))
                .collect(Collectors.toList());
        Trip incompleteTrip = firstUserTrips.get(0);
        assertEquals("$7.30", incompleteTrip.getChargeAmount());
        assertEquals(TripStatus.INCOMPLETE, incompleteTrip.getTripStatus());
        assertEquals("Stop1", incompleteTrip.getFromStopId());
        assertEquals(StringUtils.EMPTY, incompleteTrip.getToStopId());

        Trip completeTrip = firstUserTrips.get(1);
        assertEquals("$5.50", completeTrip.getChargeAmount());
        assertEquals(TripStatus.COMPLETED, completeTrip.getTripStatus());
        assertEquals("Stop2", completeTrip.getFromStopId());
        assertEquals("Stop3", completeTrip.getToStopId());
    }

    @Test
    void fromGivenTaps__SecondUserHasCompleteTripAndCancelledTrip() {
        List<Trip> secondUserTrips = writtenTrips.stream()
                .filter(trip -> trip.getPrimaryAccountNumber().equals("5555555555554444"))
                .collect(Collectors.toList());
        Trip cancelledTrip = secondUserTrips.get(0);
        assertEquals("$0.00", cancelledTrip.getChargeAmount());
        assertEquals(TripStatus.CANCELLED, cancelledTrip.getTripStatus());
        assertEquals("Stop2", cancelledTrip.getFromStopId());
        assertEquals("Stop2", cancelledTrip.getToStopId());

        Trip completeTrip = secondUserTrips.get(1);
        assertEquals("$7.30", completeTrip.getChargeAmount());
        assertEquals(TripStatus.COMPLETED, completeTrip.getTripStatus());
        assertEquals("Stop3", completeTrip.getFromStopId());
        assertEquals("Stop1", completeTrip.getToStopId());
    }
}
