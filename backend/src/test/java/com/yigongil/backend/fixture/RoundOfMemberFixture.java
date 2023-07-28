package com.yigongil.backend.fixture;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import java.util.ArrayList;

public enum RoundOfMemberFixture {

    김진우_라운드_삼(1L, MemberFixture.김진우.toMember(), false),
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
                            .optionalTodos(new ArrayList<>())
                            .isDone(isDone)
                            .build();
    }
}
