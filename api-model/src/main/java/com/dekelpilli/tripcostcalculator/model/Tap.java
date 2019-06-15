package com.dekelpilli.tripcostcalculator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

@Getter
@Setter
public class Tap {

    @JsonProperty("ID")
    private Integer id;
    @JsonProperty("DateTimeUTC")
    private Date tapTime;
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

    @JsonCreator
    public Tap(@JsonProperty("ID") Integer id,
               @JsonProperty("DateTimeUTC") Date tapTime,
               @JsonProperty("TapType") TapType tapType,
               @JsonProperty("StopId") String stopId,
               @JsonProperty("CompanyId") String companyId,
               @JsonProperty("BusID") String busId,
               @JsonProperty("PAN") String primaryAccountNumber) {
        this.id = id;
        this.tapTime = tapTime;
        this.tapType = tapType;
        this.stopId = stopId;
        this.companyId = companyId;
        this.busId = busId;
        this.primaryAccountNumber = primaryAccountNumber;
    }
}
