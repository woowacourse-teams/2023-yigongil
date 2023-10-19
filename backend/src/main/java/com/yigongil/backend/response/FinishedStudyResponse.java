package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FinishedStudyResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "자바스크립트 공부해")
        String name,
        @Schema(example = "4")
        Integer averageTier,
        @Schema(example = "3")
        Integer numberOfCurrentMembers,
        @Schema(example = "4")
        Integer numberOfMaximumMembers,
        @Schema(example = "true")
        Boolean isSucceed
) {

}
