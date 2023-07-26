package com.yigongil.backend.domain.round;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.exception.NecessaryTodoAlreadyExistException;
import com.yigongil.backend.exception.NotStudyMasterException;
import com.yigongil.backend.exception.NotStudyMemberException;
import com.yigongil.backend.fixture.RoundFixture;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class RoundTest {

    @Nested
    class 필수_투두_생성 {

        @Test
        void 정상생성한다() {
            //given
            Round round = RoundFixture.아이디_삼_투두없는_라운드.toRound();

            //then
            round.createNecessaryTodo(round.getMaster(), "필수 투두");

            //when
            assertThat(round.getNecessaryToDoContent()).isEqualTo("필수 투두");
        }

        @Test
        void 스터디_마스터가_아니라면_예외를_던진다() {
            //given
            Round round = RoundFixture.아이디_삼_투두없는_라운드.toRound();
            final Member another = Member.builder()
                    .id(3L)
                    .introduction("소개")
                    .nickname("일반_유저")
                    .build();

            //when
            final ThrowingCallable throwable = () -> round.createNecessaryTodo(another, "필수 투두");

            //then
            assertThatThrownBy(throwable).isInstanceOf(NotStudyMasterException.class);
        }

        @Test
        void 필수_투두가_존재하면_예외를_던진다() {
            //given
            Round round = RoundFixture.아이디_삼_투두없는_라운드.toRoundWithContent("필수 투두");

            //then
            ThrowingCallable throwable = () -> round.createNecessaryTodo(round.getMaster(), "새로운 투두");

            //then
            assertThatThrownBy(throwable).isInstanceOf(NecessaryTodoAlreadyExistException.class);
        }


        @Test
        void 길이가_20자를_넘으면_예외를_던진다() {
            //given
            final Round round = RoundFixture.아이디_삼_투두없는_라운드.toRound();

            //when
            ThrowingCallable throwable = () -> round.createNecessaryTodo(round.getMaster(), "굉장히긴투두길이입니다이십자도넘을수도있어");

            //then
            assertThatThrownBy(throwable).isInstanceOf(InvalidTodoLengthException.class);
        }
    }

    @Nested
    class 선택_투두_생성 {

        @Test
        void 정상생성한다() {
            //given
            Round round = RoundFixture.아이디_삼_투두없는_라운드.toRoundWithContent("필수 투두");

            //when
            OptionalTodo actual = round.createOptionalTodo(round.getMaster(), "선택 투두");

            //when
            List<OptionalTodo> optionalTodos = round.findRoundOfMemberBy(round.getMaster()).getOptionalTodos();
            assertAll(
                    () -> assertThat(optionalTodos).hasSize(1),
                    () -> assertThat(optionalTodos.get(0)).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(actual)
            );
        }

        @Test
        void 스터디_구성원이_아니라면_예외를_던진다() {
            //given
            Round round = RoundFixture.아이디_삼_투두없는_라운드.toRound();
            Member stranger = Member.builder()
                    .id(3L)
                    .nickname("예외_유저")
                    .introduction("소")
                    .build();

            //when
            ThrowingCallable throwable = () -> round.createOptionalTodo(stranger, "선택 투두");

            //then
            assertThatThrownBy(throwable)
                    .isInstanceOf(NotStudyMemberException.class);
        }
    }
}