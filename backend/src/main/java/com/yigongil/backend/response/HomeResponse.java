package com.yigongil.backend.response;

import java.util.List;

public record HomeResponse(
        Long myId,
        String nickname,
        List<UpcomingStudyResponse> studies
) {
}
