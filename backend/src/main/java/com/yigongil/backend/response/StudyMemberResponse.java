package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;

import java.util.List;

public record StudyMemberResponse(
        Long id,
        Integer tier,
        String nickname,
        Integer successRate,
        String profileImage
) {

    public static StudyMemberResponse from(Member member) {
        return new StudyMemberResponse(
                member.getId(),
                member.getTier(),
                member.getNickname(),
                member.calculateSuccessRate(),
                member.getProfileImageUrl()
        );
    }

    public static List<StudyMemberResponse> from(List<Member> members) {
        return members.stream()
                .map(StudyMemberResponse::from)
                .toList();
    }
}
