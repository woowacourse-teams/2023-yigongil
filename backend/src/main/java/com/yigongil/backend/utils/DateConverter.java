package com.yigongil.backend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    private static final int BEGIN_INDEX = 0;
    private static final int END_INDEX = 10;
    private static final String DEFAULT_DELIMITER = "-";
    private static final String NEW_DELIMITER = ".";

    private DateConverter() {
    }

    public static LocalDateTime toLocalDateTime(String dateString) {
        String pattern = "yyyy.MM.dd";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate ld = LocalDate.parse(dateString, formatter);
        return LocalDateTime.of(ld, LocalTime.MIDNIGHT);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.toString().substring(BEGIN_INDEX, END_INDEX).replaceAll(DEFAULT_DELIMITER, NEW_DELIMITER);
    }
}
