package com.yigongil.backend.domain.study;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PeriodUnitTest {

    @CsvSource({"12w,WEEK", "57d,DAY"})
    @ParameterizedTest
    void 입력을_받아서_적절히_변환한다(String input, PeriodUnit expectedPeriodUnit) {
        // given
        int expectedNumber = Integer.parseInt(input.substring(0, input.length() - 1));

        // when
        Integer periodNumber = PeriodUnit.getPeriodNumber(input);
        PeriodUnit periodUnit = PeriodUnit.getPeriodUnit(input);

        // then
        assertThat(periodNumber).isEqualTo(expectedNumber);
        assertThat(periodUnit).isEqualTo(expectedPeriodUnit);
    }

    @CsvSource({"12w,12,WEEK", "57d,57,DAY"})
    @ParameterizedTest
    void 있는_데이터로_반환_데이터를_만든다(String expected, int periodNumber, PeriodUnit periodUnit) {
        // given

        // when
        String actual = periodUnit.toStringFormat(periodNumber);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
