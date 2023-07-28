package com.yigongil.backend.domain.study;

import lombok.Getter;

@Getter
public enum ProcessingStatus {
    RECRUITING(0),
    PROCESSING(1),
    END(2),
    ;

    private final int code;

    ProcessingStatus(int code) {
        this.code = code;
    }
}
