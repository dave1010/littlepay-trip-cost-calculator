package com.dekelpilli.tripcostcalculator.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class Trip {
    private OffsetDateTime started;
    private OffsetDateTime finished;
    private Long durationSeconds;
    private String fromStopId;
    private String toStopId;
    private String chargeAmount;
    private String companyId;
    private String busId;
    private String primaryAccountNumber;
    private String status;
}
