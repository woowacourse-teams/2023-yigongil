package com.yigongil.backend.response;

public record RecruitingStudyMemberResponse(
        Long id,
        Integer tier,
        String nickname,
        Integer successRate,
        String profileImage
) {
}
