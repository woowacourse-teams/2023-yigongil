package com.yigongil.backend.domain.round;

import java.time.LocalDate;

@FunctionalInterface
public interface DatePublisher {

    LocalDate publish();
}
