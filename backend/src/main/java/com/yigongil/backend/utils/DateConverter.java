package com.yigongil.backend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private DateConverter() {
    }

    public static LocalDateTime toLocalDateTime(String dateString) {
        String pattern = "yyyy.MM.dd";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate ld = LocalDate.parse(dateString, formatter);
        return LocalDateTime.of(ld, LocalTime.MIDNIGHT);
    }
}
