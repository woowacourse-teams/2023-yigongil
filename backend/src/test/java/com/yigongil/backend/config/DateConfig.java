package com.yigongil.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Configuration
public class DateConfig {

    @Bean
    public LocalDate roundUpdateDate() {
        return LocalDate.now()
                        .plus(1L, ChronoUnit.DAYS);
    }
}
