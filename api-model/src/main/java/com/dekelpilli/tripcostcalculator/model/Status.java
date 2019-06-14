package com.dekelpilli.tripcostcalculator.model;

import lombok.Getter;

public enum Status {

    COMPLETED("COMPLETED"), INCOMPLETE("INCOMPLETE"), CANCELLED("CANCELLED");

    @Getter
    private final String status;

    Status(String status) {
        this.status = status;
    }
}
