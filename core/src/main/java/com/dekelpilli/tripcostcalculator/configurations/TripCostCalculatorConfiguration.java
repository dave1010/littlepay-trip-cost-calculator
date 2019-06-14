package com.dekelpilli.tripcostcalculator.configurations;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "trip-cost-calculator")
public class TripCostCalculatorConfiguration {
    private String input;
}
