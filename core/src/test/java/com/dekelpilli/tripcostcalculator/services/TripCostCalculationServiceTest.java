package com.dekelpilli.tripcostcalculator.services;


import com.dekelpilli.tripcostcalculator.calcluators.TripCostCalculator;
import com.dekelpilli.tripcostcalculator.configurations.TripCostCalculatorConfiguration;
import com.dekelpilli.tripcostcalculator.factories.TripCostCalculatorFactory;
import com.dekelpilli.tripcostcalculator.io.CsvFileReader;
import com.dekelpilli.tripcostcalculator.io.CsvFileWriter;
import com.dekelpilli.tripcostcalculator.model.Tap;
import com.dekelpilli.tripcostcalculator.model.TapType;
import com.dekelpilli.tripcostcalculator.model.Trip;
import com.dekelpilli.tripcostcalculator.model.TripStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@MockitoSettings
class TripCostCalculationServiceTest {

    private static final String INPUT = RandomStringUtils.randomAlphabetic(10);
    private static final String OUTPUT = RandomStringUtils.randomAlphabetic(9);
    private static final String CURRENCY = RandomStringUtils.random(1);

    @Mock
    private CsvFileReader csvFileReader;

    @Mock
    private CsvFileWriter csvFileWriter;

    @Mock
    private TripCostCalculatorFactory tripCostCalculatorFactory;

    @Mock
    private TripCostCalculatorConfiguration tripCostCalculatorConfiguration;

    @Mock
    private TripCostCalculator tripCostCalculator;

    private TripCostCalculationService tripCostCalculationService;

    @BeforeEach
    void setup() {
        when(tripCostCalculatorConfiguration.getInput()).thenReturn(INPUT);
        when(tripCostCalculatorConfiguration.getOutput()).thenReturn(OUTPUT);
        when(tripCostCalculatorConfiguration.getCurrencySymbol()).thenReturn(CURRENCY);

        tripCostCalculationService = new TripCostCalculationService(tripCostCalculatorConfiguration,
                tripCostCalculatorFactory, csvFileReader, csvFileWriter);
    }

    @Test
    void givenDataForSingleCompleteTrip__GetsCompletedTripCostForGivenStopsAndWritesToFile() throws IOException {

        String stop1 = RandomStringUtils.randomAlphabetic(10);
        String stop2 = RandomStringUtils.randomAlphabetic(9);
        String companyId = RandomStringUtils.randomAlphabetic(8);
        String busId = RandomStringUtils.randomAlphabetic(7);
        String pan = RandomStringUtils.randomAlphabetic(6);

        Instant startTime = Instant.now();
        int tripDuration = nextInt(0, 60000);

        Tap tapOn = new Tap(nextInt(), Date.from(startTime), TapType.ON, stop1, companyId, busId, pan);
        Tap tapOff = new Tap(nextInt(), Date.from(startTime.plusSeconds(tripDuration)), TapType.OFF, stop2, companyId, busId, pan);

        when(csvFileReader.parse(INPUT, Tap.class))
                .thenReturn(List.of(tapOn, tapOff).iterator());
        when(tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.COMPLETED)).thenReturn(tripCostCalculator);
        when(tripCostCalculator.calculateChargeAmount(tapOn, tapOff)).thenReturn(BigDecimal.TEN);

        tripCostCalculationService.calculateTripCosts();
        ArgumentCaptor<List<Trip>> tripArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(csvFileWriter).createFile(eq(OUTPUT), tripArgumentCaptor.capture(), eq(Trip.class));
        verify(tripCostCalculator).calculateChargeAmount(tapOn, tapOff);
        assertEquals(1, tripArgumentCaptor.getValue().size());
        Trip writtenTrip = tripArgumentCaptor.getValue().get(0);
        assertEquals(busId, writtenTrip.getBusId());
        assertEquals(stop1, writtenTrip.getFromStopId());
        assertEquals(stop2, writtenTrip.getToStopId());
        assertEquals(companyId, writtenTrip.getCompanyId());
        assertEquals(pan, writtenTrip.getPrimaryAccountNumber());
        assertEquals(tripDuration, writtenTrip.getDurationSeconds());
        assertEquals(Date.from(startTime), writtenTrip.getStartedTime());
        assertEquals(Date.from(startTime.plusSeconds(tripDuration)), writtenTrip.getFinishedTime());
        assertEquals(TripStatus.COMPLETED, writtenTrip.getTripStatus());
        assertEquals(CURRENCY + BigDecimal.TEN.setScale(2), writtenTrip.getChargeAmount());
    }

    @Test
    void givenDataForSingleCancelledTrip__GetsCancelledTripCostForGivenStopsAndWritesToFile() throws IOException {

        String stop = RandomStringUtils.randomAlphabetic(10);
        String companyId = RandomStringUtils.randomAlphabetic(8);
        String busId = RandomStringUtils.randomAlphabetic(7);
        String pan = RandomStringUtils.randomAlphabetic(6);

        Instant startTime = Instant.now();
        int tripDuration = nextInt(0, 60000);

        Tap tapOn = new Tap(nextInt(), Date.from(startTime), TapType.ON, stop, companyId, busId, pan);
        Tap tapOff = new Tap(nextInt(), Date.from(startTime.plusSeconds(tripDuration)), TapType.OFF, stop, companyId, busId, pan);

        when(csvFileReader.parse(INPUT, Tap.class))
                .thenReturn(List.of(tapOn, tapOff).iterator());
        when(tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.CANCELLED)).thenReturn(tripCostCalculator);
        when(tripCostCalculator.calculateChargeAmount(tapOn, tapOff)).thenReturn(BigDecimal.ZERO);

        tripCostCalculationService.calculateTripCosts();
        ArgumentCaptor<List<Trip>> tripArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(csvFileWriter).createFile(eq(OUTPUT), tripArgumentCaptor.capture(), eq(Trip.class));
        verify(tripCostCalculator).calculateChargeAmount(tapOn, tapOff);
        assertEquals(1, tripArgumentCaptor.getValue().size());
        Trip writtenTrip = tripArgumentCaptor.getValue().get(0);
        assertEquals(busId, writtenTrip.getBusId());
        assertEquals(stop, writtenTrip.getFromStopId());
        assertEquals(stop, writtenTrip.getToStopId());
        assertEquals(companyId, writtenTrip.getCompanyId());
        assertEquals(pan, writtenTrip.getPrimaryAccountNumber());
        assertEquals(tripDuration, writtenTrip.getDurationSeconds());
        assertEquals(Date.from(startTime), writtenTrip.getStartedTime());
        assertEquals(Date.from(startTime.plusSeconds(tripDuration)), writtenTrip.getFinishedTime());
        assertEquals(TripStatus.CANCELLED, writtenTrip.getTripStatus());
        assertEquals(CURRENCY + BigDecimal.ZERO.setScale(2), writtenTrip.getChargeAmount());
    }

    @Test
    void givenSingleTapOn__GetsIncompleteTripCostForGivenStopAndWritesToFile() throws IOException {

        String stop = RandomStringUtils.randomAlphabetic(10);
        String companyId = RandomStringUtils.randomAlphabetic(8);
        String busId = RandomStringUtils.randomAlphabetic(7);
        String pan = RandomStringUtils.randomAlphabetic(6);

        Instant startTime = Instant.now();

        Tap tapOn = new Tap(nextInt(), Date.from(startTime), TapType.ON, stop, companyId, busId, pan);

        when(csvFileReader.parse(INPUT, Tap.class))
                .thenReturn(List.of(tapOn).iterator());
        when(tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.INCOMPLETE)).thenReturn(tripCostCalculator);
        BigDecimal cost = BigDecimal.valueOf(5L);
        when(tripCostCalculator.calculateChargeAmount(tapOn, null)).thenReturn(cost);

        tripCostCalculationService.calculateTripCosts();
        ArgumentCaptor<List<Trip>> tripArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(csvFileWriter).createFile(eq(OUTPUT), tripArgumentCaptor.capture(), eq(Trip.class));
        verify(tripCostCalculator).calculateChargeAmount(tapOn, null);
        assertEquals(1, tripArgumentCaptor.getValue().size());
        Trip writtenTrip = tripArgumentCaptor.getValue().get(0);
        assertEquals(busId, writtenTrip.getBusId());
        assertEquals(stop, writtenTrip.getFromStopId());
        assertNull(writtenTrip.getToStopId());
        assertEquals(companyId, writtenTrip.getCompanyId());
        assertEquals(pan, writtenTrip.getPrimaryAccountNumber());
        assertNull(writtenTrip.getDurationSeconds());
        assertEquals(Date.from(startTime), writtenTrip.getStartedTime());
        assertNull(writtenTrip.getFinishedTime());
        assertEquals(TripStatus.INCOMPLETE, writtenTrip.getTripStatus());
        assertEquals(CURRENCY + cost.setScale(2), writtenTrip.getChargeAmount());
    }

    @Test
    void givenTwoTapOnsAndOneOffForSamePAN__GetsIncompleteTripCostAndCompleteTripCostAndWritesToFile() throws IOException {

        String stop1 = "stop1";
        String stop2 = "stop2";
        String stop3 = "stop3";
        String companyId = RandomStringUtils.randomAlphabetic(8);
        String busId = RandomStringUtils.randomAlphabetic(7);
        String busId2 = RandomStringUtils.randomAlphabetic(5);
        String pan = RandomStringUtils.randomAlphabetic(6);

        Instant startTime = Instant.now();

        Tap tapOn1 = new Tap(nextInt(), Date.from(startTime), TapType.ON, stop1, companyId, busId, pan);
        Tap tapOn2 = new Tap(nextInt(), Date.from(startTime.plusSeconds(1)), TapType.ON, stop2, companyId, busId2, pan);
        Tap tapOff = new Tap(nextInt(), Date.from(startTime.plusSeconds(2)), TapType.OFF, stop3, companyId, busId2, pan);

        when(csvFileReader.parse(INPUT, Tap.class))
                .thenReturn(List.of(tapOn1, tapOn2, tapOff).iterator());
        when(tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.INCOMPLETE)).thenReturn(tripCostCalculator);
        TripCostCalculator completedTripCostCalculator = mock(TripCostCalculator.class);
        when(tripCostCalculatorFactory.getCalculatorForStatus(TripStatus.COMPLETED)).thenReturn(completedTripCostCalculator);
        BigDecimal cost = BigDecimal.valueOf(5L);
        when(tripCostCalculator.calculateChargeAmount(tapOn1, null)).thenReturn(cost);
        when(completedTripCostCalculator.calculateChargeAmount(tapOn2, tapOff)).thenReturn(BigDecimal.TEN);

        tripCostCalculationService.calculateTripCosts();
        ArgumentCaptor<List<Trip>> tripArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(csvFileWriter).createFile(eq(OUTPUT), tripArgumentCaptor.capture(), eq(Trip.class));
        verify(tripCostCalculator).calculateChargeAmount(tapOn1, null);
        verify(completedTripCostCalculator).calculateChargeAmount(tapOn2, tapOff);
        assertEquals(2, tripArgumentCaptor.getValue().size());
        Trip writtenIncompleteTrip = tripArgumentCaptor.getValue().get(0);
        assertEquals(busId, writtenIncompleteTrip.getBusId());
        assertEquals(stop1, writtenIncompleteTrip.getFromStopId());
        assertNull(writtenIncompleteTrip.getToStopId());
        assertEquals(companyId, writtenIncompleteTrip.getCompanyId());
        assertEquals(pan, writtenIncompleteTrip.getPrimaryAccountNumber());
        assertNull(writtenIncompleteTrip.getDurationSeconds());
        assertEquals(Date.from(startTime), writtenIncompleteTrip.getStartedTime());
        assertNull(writtenIncompleteTrip.getFinishedTime());
        assertEquals(TripStatus.INCOMPLETE, writtenIncompleteTrip.getTripStatus());
        assertEquals(CURRENCY + cost.setScale(2), writtenIncompleteTrip.getChargeAmount());

        Trip writtenCompletedTrip = tripArgumentCaptor.getValue().get(1);
        assertEquals(TripStatus.COMPLETED, writtenCompletedTrip.getTripStatus());
        assertEquals(CURRENCY + BigDecimal.TEN.setScale(2), writtenCompletedTrip.getChargeAmount());
    }
}
