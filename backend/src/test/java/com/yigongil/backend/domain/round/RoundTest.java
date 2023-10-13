package com.yigongil.backend.domain.round;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.exception.NecessaryTodoAlreadyExistException;
import com.yigongil.backend.exception.NotStudyMasterException;
import com.yigongil.backend.fixture.RoundFixture;
import com.yigongil.backend.fixture.RoundOfMemberFixture;
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
            assertThat(round.getMustDo()).isEqualTo("필수 투두");
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
            Round round = RoundFixture.아이디없는_라운드.toRound();

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

    @Test
    void 멤버의_필수_투두_완료여부를_반환한다() {
        //given
        Round round = RoundFixture.아이디없는_라운드.toRound();
        Member master = round.getMaster();

        //when
        round.updateNecessaryTodoIsDone(master, true);

        //then
        assertThat(round.isNecessaryToDoDone(master)).isTrue();
    }

    @Nested
    class 투두_진행률_계산 {

        @Test
        void 멤버들의_진행률을_계산한다() {
            //given
            Round round = RoundFixture.아이디없는_라운드.toRoundWithRoundOfMember(
                    RoundOfMemberFixture.노이만_라오멤,
                    RoundOfMemberFixture.노이만_라오멤,
                    RoundOfMemberFixture.노이만_라오멤
            );
            round.getRoundOfMembers().get(0).updateNecessaryTodoIsDone(true);

            //when
            int result = round.calculateProgress();

            //then
            assertThat(result).isEqualTo(33);
        }

        @Test
        void 투두를_완료한_멤버가_없다() {
            //given
            Round round = RoundFixture.아이디없는_라운드.toRoundWithRoundOfMember(
                    RoundOfMemberFixture.노이만_라오멤,
                    RoundOfMemberFixture.노이만_라오멤,
                    RoundOfMemberFixture.노이만_라오멤
            );

            //when
            int result = round.calculateProgress();

            //then
            assertThat(result).isZero();
        }
    }
}
