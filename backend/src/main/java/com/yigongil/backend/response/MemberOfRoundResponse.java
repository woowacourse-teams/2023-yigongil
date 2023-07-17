package com.yigongil.backend.response;

public record MemberOfRoundResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        Boolean isDone
) {
}
