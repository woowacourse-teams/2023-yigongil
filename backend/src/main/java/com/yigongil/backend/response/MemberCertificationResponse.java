package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.RoundOfMember;
import java.util.List;

public record MemberCertificationResponse(
        Long id,
        String nickname,
        String profileImageUrl,
        Boolean isCertified
) {

    public static List<MemberCertificationResponse> of(List<RoundOfMember> roundOfMembers) {
        return roundOfMembers.stream()
                             .map(roundOfMember -> new MemberCertificationResponse(
                                     roundOfMember.getMember().getId(),
                                     roundOfMember.getMember().getNickname(),
                                     roundOfMember.getMember().getProfileImageUrl(),
                                     roundOfMember.isDone())
                             )
                             .toList();
    }
}
