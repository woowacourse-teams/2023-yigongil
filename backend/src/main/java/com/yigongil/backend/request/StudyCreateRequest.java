package com.yigongil.backend.request;

import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;

public record StudyCreateRequest(
        String name,
        Integer numberOfMaximumMembers,
        @FutureOrPresent(message = "예상시작일은 과거로 설정할 수 없습니다.")
        LocalDate startAt,
        Integer totalRoundCount,
        String periodOfRound,
        String introduction
) {

}
