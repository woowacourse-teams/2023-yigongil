package com.yigongil.backend.response;

import java.util.List;

public record ProfileResponse(
        Long id,
        String nickname,
        String githubId,
        String profileImageUrl,
        Double successRate,
        Integer successfulRoundCount,
        Integer tierProgress,
        Integer tier,
        String introduction,
        List<FinishedStudyResponse> finishedStudies
) {
}
