package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProgressRateResponse(
        @Schema(example = "66")
        Integer progressRate
) {

}
