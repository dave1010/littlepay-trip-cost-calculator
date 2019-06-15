package com.dekelpilli.tripcostcalculator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class Trip {
    private Date started;
    private Date finished;
    private Long durationSeconds;
    private String fromStopId;
    private String toStopId;
    private String chargeAmount;
    private String companyId;
    private String busId;
    private String primaryAccountNumber;
    private Status tripStatus;
}
