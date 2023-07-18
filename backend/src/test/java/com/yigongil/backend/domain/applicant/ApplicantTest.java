package com.yigongil.backend.domain.applicant;

import static com.yigongil.backend.domain.applicant.Applicant.builder;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.exception.StudyMemberAlreadyExistException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.StudyFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Applicant 도메인의 ")
class ApplicantTest {

    @Nested
    class 신청_유효성_검사 {

        @Test
        void 스터디에_정상적으로_참여_신청한다() {
            // given
            Study study = StudyFixture.자바_스터디.toStudy();
            Member member = MemberFixture.폰노이만.toMember();

            // when, then
            assertDoesNotThrow(
                    () -> Applicant.builder()
                            .member(member)
                            .study(study)
                            .build());
        }

        @Test
        void 이미_스터디의_Member라면_예외가_발생한다() {
            // given
            Study study = StudyFixture.자바_스터디.toStudy();
            Member masterOfStudy = MemberFixture.김진우.toMember();

            // when, then
            assertThatThrownBy(
                    () -> builder()
                            .member(masterOfStudy)
                            .study(study)
                            .build())
                    .isInstanceOf(StudyMemberAlreadyExistException.class);
        }
    }

}
