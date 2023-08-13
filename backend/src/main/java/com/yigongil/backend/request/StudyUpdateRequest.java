package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public record StudyUpdateRequest(
        @Schema(description = "스터디 제목", example = "스웨거 스터디 하실분")
        @NotBlank(message = "스터디 이름이 공백입니다.")
        String name,

        @Schema(description = "스터디 정원", example = "5")
        @Positive(message = "스터디 멤버 정원은 음수일 수 없습니다.")
        Integer numberOfMaximumMembers,

        @Schema(description = "예상 시작일", example = "2024.9.10")
        @FutureOrPresent(message = "예상시작일은 과거로 설정할 수 없습니다.")
        LocalDate startAt,

        @Schema(description = "스터디 총 회차", example = "8")
        @Positive(message = "스터디 총 회차가 입력되지 않았습니다.")
        Integer totalRoundCount,

        @Schema(description = "스터디 주기", example = "1w")
        @NotBlank(message = "스터디 주기가 입력되지 않았습니다.")
        String periodOfRound,

        @Schema(description = "스터디 소개", example = "스웨거 재미없어요")
        @NotBlank(message = "스터디 소개가 공백입니다.")
        String introduction
) {

}
