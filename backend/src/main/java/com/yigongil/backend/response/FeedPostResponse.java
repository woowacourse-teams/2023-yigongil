package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FeedPostResponse(
        @Schema(example = "1")
        Long id,
        SimpleMemberResponse author,
        @Schema(example = "http://image.png")
        String imageUrl,
        @Schema(example = "진우는 모든 것을 잘합니다")
        String content,
        @Schema(example = "5")
        Integer likeCount,
        @Schema(example = "3")
        Integer commentCount,
        @Schema(example = "true")
        Boolean forCertification,
        @Schema(example = "2021.08.12T12:00:00")
        String createdAt
) {

}
