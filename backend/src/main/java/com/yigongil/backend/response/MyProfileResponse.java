package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MyProfileResponse(
        @Schema(example = "3")
        Long id,
        @Schema(example = "김진우엄마")
        String nickname,
        @Schema(example = "gitgitgit")
        String githubId,
        @Schema(example = "http://jiwnkim.com/image")
        String profileImageUrl,
        @Schema(example = "90.0134")
        Double successRate,
        @Schema(example = "15")
        Integer successfulRoundCount,
        @Schema(example = "99")
        Integer tierProgress,
        @Schema(example = "5")
        Integer tier,
        @Schema(example = "진우 어머니에요")
        String introduction
) {

}
