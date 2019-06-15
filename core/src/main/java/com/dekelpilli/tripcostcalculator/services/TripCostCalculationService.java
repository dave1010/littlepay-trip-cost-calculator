package com.dekelpilli.tripcostcalculator.services;

import com.dekelpilli.tripcostcalculator.calcluators.TripCostCalculator;
import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.factories.TripCostCalculatorFactory;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.Trip;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TripCostCalculationService {

    private final CsvFileReader csvFileReader;
    private final TripCostCalculatorFactory tripCostCalculatorFactory;

    private final String inputFileName;
    private final String currencySymbol;

    public TripCostCalculationService(TripCostCalculatorConfiguration tripCostCalculatorConfiguration,
                                      TripCostCalculatorFactory tripCostCalculatorFactory,
                                      CsvFileReader csvFileReader) {
        inputFileName = tripCostCalculatorConfiguration.getInput();
        currencySymbol = tripCostCalculatorConfiguration.getCurrencySymbol();

        this.csvFileReader = csvFileReader;
        this.tripCostCalculatorFactory = tripCostCalculatorFactory;
    }

    public void calculateTripCosts() {
        Iterator<Tap> taps = csvFileReader.parse(inputFileName, Tap.class);

        Map<String, Tap> touchedOnUsers = new HashMap<>();
        List<Trip> trips = new ArrayList<>();
        taps.forEachRemaining(tap -> {
            String primaryAccountNumber = tap.getPrimaryAccountNumber();
            switch (tap.getTapType()) {
                case ON:
                    //TODO: if different busId, previous trip is also incomplete
                    touchedOnUsers.put(primaryAccountNumber, tap);
                    break;
                case OFF:
                    Tap tapOn = touchedOnUsers.get(primaryAccountNumber);
                    Trip trip = createTripFromTapPair(tapOn, tap);
                    trips.add(trip);
                    touchedOnUsers.remove(primaryAccountNumber);
                    break;
            }
        });
        touchedOnUsers.values().forEach(tapOn ->
                trips.add(createTripFromTapPair(tapOn, null, TripStatus.INCOMPLETE))
        );
        System.out.println(); //TODO: write trips
    }

    private Trip createTripFromTapPair(Tap tapOn, @Nullable Tap tapOff, TripStatus tripStatus) {
        Trip trip = new Trip();
        Date tapOnTime  = tapOn.getTapTime();
        trip.setStarted(tapOnTime);
        if (tapOff != null) {
            Date tapOffTime = tapOff.getTapTime();
            trip.setFinished(tapOffTime);
            trip.setDurationSeconds(calculateTripDurationInSeconds(tapOnTime, tapOffTime));

            trip.setToStopId(tapOff.getStopId());
        }

        trip.setBusId(tapOn.getBusId());
        trip.setFromStopId(tapOn.getStopId());
        trip.setCompanyId(tapOn.getCompanyId());
        trip.setPrimaryAccountNumber(tapOn.getPrimaryAccountNumber());

        TripCostCalculator tripCostCalculator = tripCostCalculatorFactory.getCalculatorForStatus(tripStatus);
        trip.setChargeAmount(currencySymbol + tripCostCalculator.calculateChargeAmount(tapOn, tapOff)
                .setScale(2, RoundingMode.HALF_UP));
        trip.setTripStatus(tripStatus);
        return trip;
    }

    private Trip createTripFromTapPair(Tap tapOn, Tap tapOff) {
        return createTripFromTapPair(tapOn, tapOff, getTripStatus(tapOn, tapOff));
    }

    private static long calculateTripDurationInSeconds(Date tapOnTime, Date tapOffTime) {
        return TimeUnit.MILLISECONDS.toSeconds(tapOffTime.getTime() - tapOnTime.getTime());
    }

    private static TripStatus getTripStatus(Tap tapOn, Tap tapOff) {
        if (tapOn.getStopId().equals(tapOff.getStopId())) {
            return TripStatus.CANCELLED;
        }
        return TripStatus.COMPLETED;
    }
}
