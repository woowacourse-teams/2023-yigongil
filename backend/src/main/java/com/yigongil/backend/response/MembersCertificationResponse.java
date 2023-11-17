package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.exception.NotStudyMemberException;
import java.util.List;

public record MembersCertificationResponse(
        String studyName,
        UpcomingRoundResponse upcomingRound,
        MemberCertificationResponse me,
        List<MemberCertificationResponse> others
) {

    public static MembersCertificationResponse of(String studyName, Round upcomingRound, Member requestingMember, List<RoundOfMember> roundOfMembers) {
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
                                                       .orElseThrow(() -> new NotStudyMemberException("해당 스터디 멤버가 아닙니다", studyName));
        return new MembersCertificationResponse(
                studyName,
                new UpcomingRoundResponse(upcomingRound.getId(), upcomingRound.getWeekNumber()),
                me,
                others
        );
    }
}
