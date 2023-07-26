package com.yigongil.backend.fixture;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;

import java.time.LocalDateTime;
import java.util.List;

public enum RoundFixture {

    아이디_삼_투두없는_라운드(3L, 2, null, MemberFixture.김진우.toMember(), null),
    아이디_사_투두없는_라운드(4L, 3, null, MemberFixture.김진우.toMember(), null),
    아이디_오_투두없는_라운드(5L, 4, null, MemberFixture.김진우.toMember(), null),
    아이디없는_라운드(null, 1, "내용", MemberFixture.김진우.toMember(), null),
    아이디없는_라운드2(null, 2, "내용", MemberFixture.김진우.toMember(), null),
    아이디없는_라운드3(null, 3, "내용", MemberFixture.김진우.toMember(), null),
    아이디없는_라운드4(null, 4, "내용", MemberFixture.김진우.toMember(), null),
    ;

    private final Long id;
    private final Integer roundNumber;
    private final String content;
    private final Member master;
    private final List<RoundOfMember> roundOfMembers;

    RoundFixture(Long id, Integer roundNumber, String content, Member master, List<RoundOfMember> roundOfMembers) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.content = content;
        this.master = master;
        this.roundOfMembers = roundOfMembers;
    }

    public Round toRound() {
        return Round.builder()
                .id(id)
                .necessaryToDoContent(content)
                .roundNumber(roundNumber)
                .master(master)
                .endAt(LocalDateTime.now())
                .roundOfMembers(List.of(RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember()))
                .build();
    }

    public Round toRoundWithContent(String content) {
        return Round.builder()
                .id(id)
                .necessaryToDoContent(content)
                .roundNumber(roundNumber)
                .master(master)
                .roundOfMembers(List.of(RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember()))
                .build();
    }
}
