package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record NicknameValidationResponse(
        @Schema(example = "true")
        Boolean exists
) {

}
