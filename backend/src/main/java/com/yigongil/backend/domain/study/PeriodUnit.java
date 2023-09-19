package com.yigongil.backend.domain.study;

import com.yigongil.backend.exception.InvalidPeriodUnitException;
import java.util.Arrays;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public enum PeriodUnit {

    DAY("d", 1),
    WEEK("w", 7),
    ;

    private static final Pattern pattern = Pattern.compile("^[0-9]+[dDwW]$");

    private final String code;
    private final Integer unitNumber;

    PeriodUnit(String code, Integer unitNumber) {
        this.code = code;
        this.unitNumber = unitNumber;
    }

    public int calculatePeriodOfRoundInDays(int periodOfRound) {
        return unitNumber * periodOfRound;
    }

    public static Integer getPeriodNumber(String input) {
        validateFormat(input);
        return Integer.parseInt(input.substring(0, input.length() - 1));
    }

    public static PeriodUnit getPeriodUnit(String input) {
        validateFormat(input);
        return Arrays.stream(values())
                     .filter(periodUnit -> periodUnit.code.equalsIgnoreCase(
                             input.substring(input.length() - 1)))
                     .findAny()
                     .orElseThrow(() -> new InvalidPeriodUnitException("잘못된 기간 입력입니다.", input));
    }

    private static void validateFormat(String input) {
        if (!pattern.matcher(input).matches()) {
            throw new InvalidPeriodUnitException("잘못된 기간 입력입니다.", input);
        }
    }

    public String toStringFormat(Integer periodOfRound) {
        return periodOfRound + code;
    }
}
