package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SimpleMemberResponse(
        @Schema(example = "2")
        Long id,
        @Schema(example = "진우친구")
        String nickname,
        @Schema(example = "http://profile_image.png")
        String profileImageUrl
) {

}
