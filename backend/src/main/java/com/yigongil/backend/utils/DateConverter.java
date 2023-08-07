package com.yigongil.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private DateConverter() {
    }

    public static String toStringFormat(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER);
    }
}
