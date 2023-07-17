package com.yigongil.backend.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.yigongil.backend.exception.InvalidIntroductionLengthException;
import com.yigongil.backend.exception.InvalidNicknameLengthException;
import com.yigongil.backend.exception.InvalidNicknamePatternException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @Nested
    class 닉네임_검증 {

        @ValueSource(strings = {"짧", "엄청긴닉네임입니다"})
        @ParameterizedTest
        void 닉네임이_2자이상_8자이하가_아니면_예외를_던진다(String input) {
            //given
            //when
            ThrowingCallable throwable = () -> Member.builder()
                    .nickname(input)
                    .introduction("적절한 자기소개")
                    .build();

            //then
            assertThatThrownBy(throwable).isInstanceOf(InvalidNicknameLengthException.class);
        }

        @ValueSource(strings = {"공 백", "달러$", "  "})
        @ParameterizedTest
        void 닉네임에_한글_영어_숫자_언더바_외의_문자가_포함되면_예외를_던진다(String input) {
            //given
            //when
            ThrowingCallable throwable = () -> Member.builder()
                    .nickname(input)
                    .introduction("적절한 자기소개")
                    .build();

            //then
            assertThatThrownBy(throwable).isInstanceOf(InvalidNicknamePatternException.class);
        }

        @ValueSource(strings = {"2자", "is_eight"})
        @ParameterizedTest
        void 적절한_입력일_경우_멤버를_정상_생성한다(String input) {
            //given
            //when
            final ThrowingSupplier<Member> supplier = () -> Member.builder()
                    .nickname(input)
                    .introduction("적절한 자기소개")
                    .build();

            //then
            assertDoesNotThrow(supplier);
        }
    }

    @Nested
    class 간단소개_검증 {

        @CsvSource(value = {"글:201"}, delimiter = ':')
        @ParameterizedTest
        void 간단소개가_200자이하가_아니면_예외를_던진다(String input, int repeatCount) {
            //given
            //when
            ThrowingCallable throwable = () -> Member.builder()
                    .nickname("적절한닉네임")
                    .introduction(input.repeat(repeatCount))
                    .build();

            //then
            assertThatThrownBy(throwable).isInstanceOf(InvalidIntroductionLengthException.class);
        }

        @Test
        void 간단소개가_공백이라면_예외를_던진다() {
            //given
            //when
            ThrowingCallable throwable = () -> Member.builder()
                    .nickname("적절한닉네임")
                    .introduction("  ")
                    .build();

            //then
            assertThatThrownBy(throwable).isInstanceOf(InvalidIntroductionLengthException.class);
        }

        @CsvSource(value = {"글:200", "글:1"}, delimiter = ':')
        @ParameterizedTest
        void 간단소개가_1자이상_200자이하이면_정상생성된다(String input, int repeatCount) {
            //given
            //when
            final ThrowingSupplier<Member> supplier = () -> Member.builder()
                    .nickname("적절한닉네임")
                    .introduction(input.repeat(repeatCount))
                    .build();

            //then
            assertDoesNotThrow(supplier);
        }
    }
}
