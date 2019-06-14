package com.dekelpilli.tripcostcalculator.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class Tap {

    @JsonAlias("ID")
    private int id;
    @JsonAlias("DateTimeUTC")
    private OffsetDateTime offsetDateTime;
    @JsonAlias("TapType")
    private TapType tapType;
    @JsonAlias("StopId")
    private String stopId;
    @JsonAlias("CompanyId")
    private String companyId;
    @JsonAlias("BusID")
    private String busId;
    @JsonAlias("PAN")
    private String primaryAccountNumber;

}
