package com.yigongil.backend.domain.study;

import com.yigongil.backend.exception.InvalidProcessingStatusException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ProcessingStatus {
    ALL(3),
    RECRUITING(0),
    PROCESSING(1),
    END(2),
    ;

    private final int code;

    ProcessingStatus(int code) {
        this.code = code;
    }

    public static ProcessingStatus of(String target) {
        return Arrays.stream(ProcessingStatus.values())
                     .filter(status -> status.name().equalsIgnoreCase(target))
                     .findAny()
                     .orElseThrow(() -> new InvalidProcessingStatusException(target));
    }
}
