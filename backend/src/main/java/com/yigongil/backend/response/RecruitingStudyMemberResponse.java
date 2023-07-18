package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;

import java.util.List;

public record RecruitingStudyMemberResponse(
        Long id,
        Integer tier,
        String nickname,
        Integer successRate,
        String profileImage
) {

    public static RecruitingStudyMemberResponse from(Member member) {
        return new RecruitingStudyMemberResponse(
                member.getId(),
                member.getTier(),
                member.getNickname(),
                member.calculateSuccessRate(),
                member.getProfileImageUrl()
        );
    }

    public static List<RecruitingStudyMemberResponse> from(List<Member> members) {
        return members.stream()
                .map(RecruitingStudyMemberResponse::from)
                .toList();
    }
}
