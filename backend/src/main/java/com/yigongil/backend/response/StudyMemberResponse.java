package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyMemberResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "4")
        Integer tier,
        @Schema(example = "zl존zin우")
        String nickname,
        @Schema(example = "100.0")
        Double successRate,
        @Schema(example = "http://jinwoo.com/image/good")
        String profileImage,
        @Schema(example = "false")
        Boolean isDeleted
) {

}
