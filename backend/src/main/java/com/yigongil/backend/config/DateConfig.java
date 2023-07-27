package com.yigongil.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DateConfig {

    @Bean
    public LocalDate roundUpdateDate() {
        return LocalDate.now();
    }
}
