package com.yigongil.backend.domain.roundofmember;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.exception.InvalidMemberInRoundException;

import java.util.List;

public class RoundOfMembers {

    private final List<RoundOfMember> roundOfMembers;

    public RoundOfMembers(List<RoundOfMember> roundOfMembers) {
        this.roundOfMembers = roundOfMembers;
    }

    public RoundOfMember findByMember(Member member) {
        return roundOfMembers.stream()
                .filter(roundOfMember -> roundOfMember.isMemberEquals(member))
                .findFirst()
                .orElseThrow(() -> new InvalidMemberInRoundException("해당 라운드에 멤버가 존재하지 않습니다.", member.getId()));
    }

    public int calculateMembersProgress() {
        long count = roundOfMembers.stream()
                .map(RoundOfMember::getDone)
                .filter(it -> it)
                .count();

        return (int) (count * 100 / roundOfMembers.size());
    }
}
