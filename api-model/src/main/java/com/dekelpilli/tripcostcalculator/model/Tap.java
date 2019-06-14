package com.dekelpilli.tripcostcalculator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class Tap {

    @JsonProperty("ID")
    private int id;
    @JsonProperty("DateTimeUTC")
    private OffsetDateTime offsetDateTime;
    @JsonProperty("TapType")
    private TapType tapType;
    @JsonProperty("StopId")
    private String stopId;
    @JsonProperty("CompanyId")
    private String companyId;
    @JsonProperty("BusID")
    private String busId;
    @JsonProperty("PAN")
    private String primaryAccountNumber;

}
