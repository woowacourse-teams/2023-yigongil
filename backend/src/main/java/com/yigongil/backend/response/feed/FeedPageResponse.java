package com.yigongil.backend.response.feed;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record FeedPageResponse(
        @Schema(example = "자바스크립트 공부해")
        String studyName,
        List<FeedPostResponse> feeds
) {

}
