package com.yigongil.backend.fixture;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;

public enum RoundOfMemberFixture {

    김진우_라운드_삼(1L, MemberFixture.김진우.toMember(), false),
    노이만_라오멤(1L, MemberFixture.폰노이만.toMember(), false),
    ;

    private final Long id;
    private final Member member;
    private final Boolean isDone;

    RoundOfMemberFixture(Long id, Member member, Boolean isDone) {
        this.id = id;
        this.member = member;
        this.isDone = isDone;
    }

    public RoundOfMember toRoundOfMember() {
        return RoundOfMember.builder()
                            .id(id)
                            .member(member)
                            .isDone(isDone)
                            .build();
    }
}
