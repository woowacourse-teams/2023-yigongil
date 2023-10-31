package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ProfileResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "진우는다알아")
        String nickname,
        @Schema(example = "kimjw23")
        String githubId,
        @Schema(example = "http://jinwoo.image.com")
        String profileImageUrl,
        @Schema(example = "20.7")
        Double successRate,
        @Schema(example = "5")
        Integer successfulRoundCount,
        @Schema(example = "25")
        Integer tierProgress,
        @Schema(example = "2")
        Integer tier,
        @Schema(example = "진우는 모든 것을 잘합니다")
        String introduction,
        List<FinishedStudyResponse> finishedStudies
) {

}
