package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpcomingStudyResponse(
        @Schema(example = "12")
        Long id,
        @Schema(example = "레스트 독스 스터디")
        String name,
        @Schema(example = "꼭 해와야 합니다")
        String todoContent,
        @Schema(example = "3")
        Integer leftDays,
        @Schema(example = "10")
        Integer grassCount,
        @Schema(example = "true")
        boolean isMaster
) {

}
