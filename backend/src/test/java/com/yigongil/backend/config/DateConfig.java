package com.yigongil.backend.config;

import com.yigongil.backend.domain.round.DatePublisher;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfig {

    @Bean
    public DatePublisher roundUpdateDate() {
        return () -> LocalDate.now()
                        .plus(1L, ChronoUnit.DAYS);
    }
}
