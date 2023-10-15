package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record StudyStartRequest(
        @Schema(description = "스터디 시작 요일", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
        List<String> meetingDaysOfTheWeek
) {

}
