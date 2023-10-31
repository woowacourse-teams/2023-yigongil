package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpcomingRoundResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "2")
        Integer weekNumber
) {

}
