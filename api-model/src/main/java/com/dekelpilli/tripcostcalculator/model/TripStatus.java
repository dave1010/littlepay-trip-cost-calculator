package com.dekelpilli.tripcostcalculator.model;

import lombok.Getter;

public enum TripStatus {

    COMPLETED("COMPLETED"), INCOMPLETE("INCOMPLETE"), CANCELLED("CANCELLED");

    @Getter
    private final String status;

    TripStatus(String status) {
        this.status = status;
    }
}
