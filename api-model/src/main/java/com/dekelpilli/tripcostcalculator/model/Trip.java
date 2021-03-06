package com.dekelpilli.tripcostcalculator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class Trip {
    @JsonProperty("Started")
    private Date startedTime;
    @JsonProperty("Finished")
    private Date finishedTime;
    @JsonProperty("DurationSecs")
    private Long durationSeconds;
    @JsonProperty("FromStopId")
    private String fromStopId;
    @JsonProperty("ToStopId")
    private String toStopId;
    @JsonProperty("ChargeAmount")
    private String chargeAmount;
    @JsonProperty("CompanyId")
    private String companyId;
    @JsonProperty("BusID")
    private String busId;
    @JsonProperty("PAN")
    private String primaryAccountNumber;
    @JsonProperty("Status")
    private TripStatus tripStatus;

    @JsonCreator
    public Trip(@JsonProperty("Started") Date startedTime,
                @JsonProperty("Finished") Date finishedTime,
                @JsonProperty("DurationSecs") Long durationSeconds,
                @JsonProperty("FromStopId") String fromStopId,
                @JsonProperty("ToStopId") String toStopId,
                @JsonProperty("ChargeAmount") String chargeAmount,
                @JsonProperty("CompanyId") String companyId,
                @JsonProperty("BusID") String busId,
                @JsonProperty("PAN") String primaryAccountNumber,
                @JsonProperty("Status") TripStatus tripStatus) {
        this.startedTime = startedTime;
        this.finishedTime = finishedTime;
        this.durationSeconds = durationSeconds;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busId = busId;
        this.primaryAccountNumber = primaryAccountNumber;
        this.tripStatus = tripStatus;
    }
}
