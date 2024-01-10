package com.yigongil.backend.domain.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.exception.InvalidMemberSizeException;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.StudyFixture;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StudyTest {

    private static final LocalDate LOCAL_DATE_OF_MONDAY = LocalDate.of(2023, 10, 9);

    @Nested
    class 스터디가_진행_중_일_때 {

        private Study study;

        @BeforeEach
        void setUp() {
            study = StudyFixture.자바_스터디_모집중.toStudy();
            Member secondMember = MemberFixture.마틴파울러.toMember();
            study.apply(secondMember);
            study.permit(secondMember, study.getMaster());
        }

        @Test
        void 스터디를_시작할_때_지원자가_스터디_멤버에서_삭제된다() {
            // given
            Member applicant = MemberFixture.폰노이만.toMember();
            study.apply(applicant);

            // when
            study.start(study.getMaster(), List.of(DayOfWeek.MONDAY),
                    LOCAL_DATE_OF_MONDAY.atStartOfDay());

            Boolean isApplicantExist = study.getStudyMembers().stream()
                                            .anyMatch(studyMember -> studyMember.getMember().equals(applicant));

            // then
            assertThat(isApplicantExist).isFalse();
        }

        @Nested
        class 스터디를_주_1회_한다면 {

            @BeforeEach
            void setUp() {
                study.start(study.getMaster(), List.of(DayOfWeek.MONDAY),
                        LOCAL_DATE_OF_MONDAY.atStartOfDay());
            }

            @Test
            void 모집중이지_않은_스터디에_Member를_추가하면_예외가_발생한다() {
                // given
                Member member = MemberFixture.폰노이만.toMember();

                // when
                // then
                assertThatThrownBy(() -> study.permit(member, study.getMaster()))
                        .isInstanceOf(InvalidProcessingStatusException.class);
            }

            @Test
            void 스터디의_상태가_모집중이_아닐_때_정보를_수정하면_예외가_발생한다() {
                // given
                // when
                ThrowingCallable throwable = () -> study.updateInformation(
                        study.getMaster(),
                        "이름 수정",
                        5,
                        "소개",
                        7,
                        3
                );

                // then
                assertThatThrownBy(throwable)
                        .isInstanceOf(InvalidProcessingStatusException.class);
            }
        }
    }

    @Test
    void 스터디에_Member를_추가한다() {
        // given
        Study study = StudyFixture.자바_스터디_모집중.toStudy();
        Member member = MemberFixture.폰노이만.toMember();
        int sizeOfCurrentMembers = study.sizeOfCurrentMembers();

        // when
        study.apply(member);
        study.permit(member, study.getMaster());
        int actual = study.sizeOfCurrentMembers();

        // then
        assertThat(actual).isEqualTo(sizeOfCurrentMembers + 1);
    }

    @Test
    void 정원이_가득_찬_스터디에_Member를_추가하면_예외가_발생한다() {
        // given
        Study study = StudyFixture.자바_스터디_모집중_정원_2.toStudy();
        Member member2 = MemberFixture.마틴파울러.toMember();
        Member member3 = MemberFixture.폰노이만.toMember();
        study.apply(member2);
        study.permit(member2, study.getMaster());

        // when
        // then
        assertThatThrownBy(() -> study.apply(member3))
                .isInstanceOf(InvalidMemberSizeException.class);
    }
}
