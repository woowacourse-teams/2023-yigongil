package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record HomeResponse(
        @Schema(example = "1")
        Long myId,
        @Schema(example = "김진김진우우")
        String nickname,
        List<UpcomingStudyResponse> studies
) {

    public static HomeResponse of(Member member, List<Study> studies, List<UpcomingStudyResponse> upcomingStudyResponses) {
        return new HomeResponse(
                member.getId(),
                member.getNickname(),
                upcomingStudyResponses
        );
    }
}
