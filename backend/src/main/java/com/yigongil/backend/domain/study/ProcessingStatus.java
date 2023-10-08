package com.yigongil.backend.domain.study;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ProcessingStatus {
    RECRUITING(0),
    PROCESSING(1),
    END(2),
    ALL(3);

    private final int code;

    ProcessingStatus(int code) {
        this.code = code;
    }

    public static void validate(String target) {
        Arrays.stream(ProcessingStatus.values())
              .filter(status -> status.name().equalsIgnoreCase(target))
              .findAny()
              .orElseThrow(RuntimeException::new);
        // TODO: 2023/10/08 예외처리
    }
}
