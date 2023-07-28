package com.yigongil.backend.response;

public record MyProfileResponse(
        Long id,
        String nickname,
        String githubId,
        String profileImageUrl,
        Double successRate,
        Integer successfulRoundCount,
        Integer tierProgress,
        Integer tier,
        String introduction
) {
}
