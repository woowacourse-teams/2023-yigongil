package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FeedPostCreateRequest(
        @Schema(description = "피드 내용", example = "피드 내용입니다.")
        String content,
        @Schema(description = "피드 이미지 URL", example = "https://yigongil.png")
        String imageUrl
) {

}
