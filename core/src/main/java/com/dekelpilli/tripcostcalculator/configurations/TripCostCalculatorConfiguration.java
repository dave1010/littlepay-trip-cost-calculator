package com.dekelpilli.tripcostcalculator.configurations;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "trip-cost-calculator")
public class TripCostCalculatorConfiguration {
    private String input;
    private String output;
    private String currencySymbol;
    private Collection<CostMapping> costMappings;

    @Setter
    @Getter
    public static class CostMapping {
        private List<String> stops;
        private BigDecimal  cost;
    }
}
