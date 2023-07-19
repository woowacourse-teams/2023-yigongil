package com.yigongil.backend.domain.roundofmember;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.exception.TooManyOptionalTodosException;
import com.yigongil.backend.fixture.RoundOfMemberFixture;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class RoundOfMemberTest {

    @Test
    void 선택_투두를_정상_생성한다() {
        //given
        RoundOfMember roundOfMember = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();

        //when
        roundOfMember.createOptionalTodo("선택 투두");

        //then
        assertAll(
                () -> assertThat(roundOfMember.getOptionalTodos()).hasSize(1),
                () -> assertThat(roundOfMember.getOptionalTodos().get(0).getContent()).isEqualTo("선택 투두")
        );
    }

    @Test
    void 선택_투두가_최대_갯수보다_많으면_예외를_던진다() {
        //given
        RoundOfMember roundOfMember = RoundOfMemberFixture.김진우_라운드_삼.toRoundOfMember();
        for (int i = 0; i < 4; i++) {
            roundOfMember.createOptionalTodo("선택 투두");
        }

        //when
        ThrowingCallable throwable = () -> roundOfMember.createOptionalTodo("예외 터짐");

        //then
        assertThatThrownBy(throwable).isInstanceOf(TooManyOptionalTodosException.class);
    }
}
