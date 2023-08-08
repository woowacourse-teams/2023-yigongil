package com.yigongil.backend.request;

import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public record StudyCreateRequest(
        @NotBlank(message = "스터디 이름이 공백입니다.")
        String name,
        @Positive(message = "스터디 멤버 정원은 음수일 수 없습니다.")
        Integer numberOfMaximumMembers,
        @FutureOrPresent(message = "예상시작일은 과거로 설정할 수 없습니다.")
        LocalDate startAt,
        @Positive(message = "스터디 총 회차가 입력되지 않았습니다.")
        Integer totalRoundCount,
        @NotBlank(message = "스터디 주기가 입력되지 않았습니다.")
        String periodOfRound,
        @NotBlank(message = "스터디 소개가 공백입니다.")
        String introduction
) {

}
