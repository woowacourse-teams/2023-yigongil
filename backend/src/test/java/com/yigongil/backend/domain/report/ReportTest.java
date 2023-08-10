package com.yigongil.backend.domain.report;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.exception.InvalidReportContentLengthException;
import com.yigongil.backend.exception.InvalidReportException;
import com.yigongil.backend.exception.InvalidReportTitleLengthException;
import com.yigongil.backend.fixture.MemberFixture;
import java.time.LocalDate;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class ReportTest {

    @Test
    void 제목이_30자를_초과하면_예외가_발생한다() {
        // given
        String invalidTitle = "민".repeat(31);

        // when
        ThrowingCallable throwable = () -> Report.builder()
                                                 .reporter(MemberFixture.김진우.toMember())
                                                 .reported(MemberFixture.폰노이만.toMember())
                                                 .title(invalidTitle)
                                                 .content("내용")
                                                 .problemOccuredAt(LocalDate.now())
                                                 .build();

        // then
        assertThatThrownBy(throwable)
                .isInstanceOf(InvalidReportTitleLengthException.class);
    }

    @Test
    void 내용이_200자를_초과하면_예외가_발생한다() {
        // given
        String invalidContent = "민".repeat(201);

        // when
        ThrowingCallable throwable = () -> Report.builder()
                                                 .reporter(MemberFixture.김진우.toMember())
                                                 .reported(MemberFixture.폰노이만.toMember())
                                                 .title("제목")
                                                 .content(invalidContent)
                                                 .problemOccuredAt(LocalDate.now())
                                                 .build();

        // then
        assertThatThrownBy(throwable)
                .isInstanceOf(InvalidReportContentLengthException.class);
    }

    @Test
    void 신고자와_피신고자가_같으면_예외가_발생한다() {
        // given
        Member member = MemberFixture.김진우.toMember();

        // when
        ThrowingCallable throwable = () -> Report.builder()
                                                 .reporter(member)
                                                 .reported(member)
                                                 .title("제목")
                                                 .content("내용")
                                                 .problemOccuredAt(LocalDate.now())
                                                 .build();

        // then
        assertThatThrownBy(throwable)
                .isInstanceOf(InvalidReportException.class);
    }
}
