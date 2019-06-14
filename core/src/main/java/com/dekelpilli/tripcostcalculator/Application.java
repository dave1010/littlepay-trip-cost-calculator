package com.dekelpilli.tripcostcalculator;

import com.dekelpilli.tripcostcalculator.services.TripCostCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@AllArgsConstructor(onConstructor_ = @Autowired)
public class Application implements CommandLineRunner {

    private final TripCostCalculationService tripCostCalculationService;

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(String... args) {
        tripCostCalculationService.calculateTripCosts();
    }
}
