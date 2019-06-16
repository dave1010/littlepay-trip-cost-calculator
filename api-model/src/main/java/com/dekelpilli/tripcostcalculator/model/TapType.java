package com.dekelpilli.tripcostcalculator.model;

import lombok.Getter;


public enum TapType {

    ON("ON"), OFF("OFF");

    @Getter
    private final String type;

    TapType(String tapeType) {
        type = tapeType;
    }
}
