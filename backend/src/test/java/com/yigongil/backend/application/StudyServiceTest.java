package com.yigongil.backend.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.yigongil.backend.domain.applicant.ApplicantRepository;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.StudyFixture;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("StudyService 클래스의 ")
class StudyServiceTest {

    private final StudyRepository studyRepository = mock(StudyRepository.class);
    private final ApplicantRepository applicantRepository = mock(ApplicantRepository.class);
    private final StudyService studyService = new StudyService(studyRepository, applicantRepository);

    @Nested
    class 스터디_지원자_유효성_검사 {
        
        @Test
        void 지원한_스터디가_존재하지_않으면_예외가_발생한다() {
            // given
            Member member = MemberFixture.김진우.toMember();
            Long nonExistStudyId = 100L;
            given(studyRepository.findById(nonExistStudyId)).willReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> studyService.apply(member, nonExistStudyId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_지원한_스터디에_다시_지원하면_예외가_발생한다() {
            // given
            Member member = MemberFixture.김진우.toMember();
            Study study = StudyFixture.자바_스터디.toStudy();
            given(studyRepository.findById(any())).willReturn(Optional.of(study));
            given(applicantRepository.existsByMemberAndStudy(member, study)).willReturn(true);

            // when, then
            assertThatThrownBy(() -> studyService.apply(member, study.getId()))
                    .isInstanceOf(ApplicantAlreadyExistException.class);
        }
    }
}
