package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MemberOfRoundResponse(
        @Schema(example = "2")
        Long id,
        @Schema(example = "진우친구")
        String nickname,
        @Schema(example = "http://swagger-ui.html")
        String profileImageUrl,
        @Schema(example = "true")
        Boolean isDone,
        @Schema(example = "false")
        Boolean isDeleted
) {

    public static MemberOfRoundResponse from(RoundOfMember roundOfMember) {
        Member member = roundOfMember.getMember();
        return new MemberOfRoundResponse(
                member.getId(),
                member.getNickname(),
                member.getProfileImageUrl(),
                roundOfMember.isDone(),
                member.isDeleted()
        );
    }

    public static List<MemberOfRoundResponse> from(List<RoundOfMember> roundOfMembers) {
        return roundOfMembers.stream()
                             .map(MemberOfRoundResponse::from)
                             .toList();
    }
}
