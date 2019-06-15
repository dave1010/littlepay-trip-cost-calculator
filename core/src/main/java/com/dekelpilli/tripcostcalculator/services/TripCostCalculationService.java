package com.dekelpilli.tripcostcalculator.services;

import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import com.dekelpilli.tripcostcalculator.model.Status;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.Trip;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TripCostCalculationService {

    private final String inputFileName;
    private final CsvFileReader csvFileReader;

    public TripCostCalculationService(TripCostCalculatorConfiguration tripCostCalculatorConfiguration,
                                      CsvFileReader csvFileReader) {
        inputFileName = tripCostCalculatorConfiguration.getInput();

        this.csvFileReader = csvFileReader;
    }

    public void calculateTripCosts() {
        Iterator<Tap> taps = csvFileReader.parse(inputFileName, Tap.class);

        Map<String, Tap> touchedOnUsers = new HashMap<>();
        List<Trip> trips = new ArrayList<>();
        taps.forEachRemaining(tap -> {
            String primaryAccountNumber = tap.getPrimaryAccountNumber();
            switch (tap.getTapType()) {
                case ON:
                    touchedOnUsers.put(primaryAccountNumber, tap);
                    break;
                case OFF:
                    Tap tapOn = touchedOnUsers.get(primaryAccountNumber);
                    // TODO: cost calc interface for each status enum
                    // TODO: mapstruct
                    Trip trip = createTripFromTapPair(tapOn, tap);
                    trips.add(trip);
                    touchedOnUsers.remove(primaryAccountNumber);
            }
        });
        //TODO: for remaining items in touchedOnUsers, calc as incomplete trips
    }

    private Trip createTripFromTapPair(Tap tapOn, Tap tapOff) {
        Trip trip = new Trip();
        Date tapOnTime  = tapOn.getTapTime();
        Date tapOffTime = tapOff.getTapTime();
        trip.setStarted(tapOnTime);
        trip.setFinished(tapOffTime);
        trip.setDurationSeconds(calculateTripDurationSeconds(tapOnTime, tapOffTime));

        trip.setBusId(tapOff.getBusId());
        trip.setFromStopId(tapOn.getStopId());
        trip.setToStopId(tapOff.getStopId());
        trip.setCompanyId(tapOff.getCompanyId());
        trip.setPrimaryAccountNumber(tapOff.getPrimaryAccountNumber());
        if (Status.CANCELLED.equals(getTripStatus(tapOn, tapOff))) {
            trip.setChargeAmount("0.00");
            trip.setTripStatus(Status.CANCELLED);
        }
        //TODO: completed
        return trip;
    }

    private static long calculateTripDurationSeconds(Date tapOnTime, Date tapOffTime) {
        return TimeUnit.MILLISECONDS.toSeconds(tapOffTime.getTime() - tapOnTime.getTime());
    }

    private static Status getTripStatus(Tap tapOn, Tap tapOff) {
        if (tapOn.getStopId().equals(tapOff.getStopId())) {
            return Status.CANCELLED;
        }
        return Status.COMPLETED;
    }
}
