package com.bellafit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BellaFitApplication {

    public static void main(String[] args) {
        SpringApplication.run(BellaFitApplication.class, args);
    }
} 