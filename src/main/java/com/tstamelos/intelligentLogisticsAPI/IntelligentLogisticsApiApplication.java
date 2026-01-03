package com.tstamelos.intelligentLogisticsAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IntelligentLogisticsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentLogisticsApiApplication.class, args);
    }

}
