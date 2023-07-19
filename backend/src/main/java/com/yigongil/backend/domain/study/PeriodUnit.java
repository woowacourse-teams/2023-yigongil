package com.yigongil.backend.domain.study;

import com.yigongil.backend.exception.InvalidPeriodUnitException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public enum PeriodUnit {

    DAY(List.of("d", "D")),
    WEEK(List.of("w", "W")),
    ;

    private static final int CODE_INDEX = 0;
    private static final Pattern pattern = Pattern.compile("^[0-9]+[dDwW]$");

    private final List<String> codes;

    PeriodUnit(List<String> codes) {
        this.codes = codes;
    }

    public static Integer getPeriodNumber(String input) {
        validateFormat(input);
        return Integer.parseInt(input.substring(0, input.length() - 1));
    }

    public static PeriodUnit getPeriodUnit(String input) {
        validateFormat(input);
        return Arrays.stream(values())
                     .filter(periodUnit -> periodUnit.codes.stream()
                                                           .anyMatch(input::endsWith))
                     .findAny()
                     .orElseThrow(() -> new InvalidPeriodUnitException("잘못된 기간 입력입니다.", input));
    }

    private static void validateFormat(String input) {
        if (!pattern.matcher(input).matches()) {
            throw new InvalidPeriodUnitException("잘못된 기간 입력입니다.", input);
        }
    }

    public String toStringFormat(Integer periodOfRound) {
        return periodOfRound + codes.get(CODE_INDEX);
    }
}
