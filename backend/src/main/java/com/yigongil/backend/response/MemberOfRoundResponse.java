package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;

import java.util.List;

public record MemberOfRoundResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        Boolean isDone
) {

    public static MemberOfRoundResponse from(RoundOfMember roundOfMember) {
        Member member = roundOfMember.getMember();
        return new MemberOfRoundResponse(
                member.getId(),
                member.getNickname(),
                member.getProfileImageUrl(),
                roundOfMember.getDone()
        );
    }

    public static List<MemberOfRoundResponse> from(List<RoundOfMember> roundOfMembers) {
        return roundOfMembers.stream()
                .map(MemberOfRoundResponse::from)
                .toList();
    }
}
