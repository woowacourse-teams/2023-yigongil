package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;

import java.util.List;

public record HomeResponse(
        Long myId,
        String nickname,
        List<UpcomingStudyResponse> studies
) {

    public static HomeResponse of(Member member, List<Study> studies) {
        return new HomeResponse(
                member.getId(),
                member.getNickname(),
                UpcomingStudyResponse.of(member, studies)
        );
    }
}
