package com.yigongil.backend.response;

public record StudyMemberResponse(
        Long id,
        Integer tier,
        String nickname,
        Integer successRate,
        String profileImage
) {
}
