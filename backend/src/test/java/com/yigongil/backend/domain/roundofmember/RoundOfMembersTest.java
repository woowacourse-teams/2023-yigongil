package com.yigongil.backend.domain.roundofmember;

import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.fixture.RoundOfMemberFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

class RoundOfMembersTest {

    @Test
    void 멤버들의_진행률을_계산한다() {
        RoundOfMember member1 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        RoundOfMember member2 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        RoundOfMember member3 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        RoundOfMember member4 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();

        member4.updateNecessaryTodoIsDone(true);

        RoundOfMembers roundOfMembers = new RoundOfMembers(List.of(member1, member2, member3, member4));

        int result = roundOfMembers.calculateMembersProgress();

        assertThat(result).isEqualTo(25);
    }

    @Test
    void 멤버들의_진행률을_계산한다2() {
        RoundOfMember member1 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        RoundOfMember member2 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        RoundOfMember member3 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        RoundOfMember member4 = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();

        member4.updateNecessaryTodoIsDone(true);
        member3.updateNecessaryTodoIsDone(true);

        RoundOfMembers roundOfMembers = new RoundOfMembers(List.of(member1, member2, member3, member4));

        int result = roundOfMembers.calculateMembersProgress();

        assertThat(result).isEqualTo(50);
    }

}
