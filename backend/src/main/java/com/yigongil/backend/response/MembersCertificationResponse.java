package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import java.util.List;

public record MembersCertificationResponse(
        MemberCertificationResponse me,
        List<MemberCertificationResponse> others
) {

    public static MembersCertificationResponse of(Member requestingMember, List<RoundOfMember> roundOfMembers) {
        List<MemberCertificationResponse> others = roundOfMembers.stream()
                                                                       .filter(roundOfMember -> !roundOfMember.isMemberEquals(requestingMember))
                                                                       .map(roundOfMember -> new MemberCertificationResponse(
                                                                               roundOfMember.getMember().getId(),
                                                                               roundOfMember.getMember().getNickname(),
                                                                               roundOfMember.getMember().getProfileImageUrl(),
                                                                               roundOfMember.isDone()))
                                                                       .toList();
        MemberCertificationResponse me = roundOfMembers.stream()
                                                             .filter(roundOfMember -> roundOfMember.isMemberEquals(requestingMember))
                                                             .findAny()
                                                             .map(roundOfMember -> new MemberCertificationResponse(
                                                                     roundOfMember.getMember().getId(),
                                                                     roundOfMember.getMember().getNickname(),
                                                                     roundOfMember.getMember().getProfileImageUrl(),
                                                                     roundOfMember.isDone()))
                                                             .get();
        return new MembersCertificationResponse(me, others);
    }
}
