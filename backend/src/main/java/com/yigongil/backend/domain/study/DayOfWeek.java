package com.yigongil.backend.domain.study;

import lombok.Getter;

@Getter
public enum DayOfWeek {

    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6),
    SUN(7),
    ;

    private final int code;

    DayOfWeek(int code) {
        this.code = code;
    }
}
