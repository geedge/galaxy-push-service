package com.nis.galaxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GalaxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyApplication.class, args);
    }
}