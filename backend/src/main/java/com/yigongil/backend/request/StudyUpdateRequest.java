package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public record StudyUpdateRequest(
        @Schema(description = "스터디 제목", example = "스웨거 스터디 하실분")
        @NotBlank(message = "스터디 이름이 공백입니다.")
        String name,

        @Schema(description = "스터디 정원", example = "5")
        @Positive(message = "스터디 멤버 정원은 음수일 수 없습니다.")
        Integer numberOfMaximumMembers,

        @Schema(description = "스터디 최소 주차", example = "7")
        @Positive(message = "스터디 최소 주차는 음수일 수 없습니다.")
        Integer minimumWeeks,

        @Schema(description = "스터디 진행 횟수", example = "3")
        @Positive(message = "스터디 진행 횟수는 음수일 수 없습니다.")
        Integer meetingDaysCountPerWeek,

        @Schema(description = "스터디 소개", example = "스웨거 재미없어요")
        @NotBlank(message = "스터디 소개가 공백입니다.")
        String introduction
) {

}
