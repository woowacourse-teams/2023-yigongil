package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.exception.InvalidMemberSizeException;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.StudyFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class StudyTest {

    @Test
    void 스터디_주기를_계산한다() {
        Study study1 = StudyFixture.자바_스터디_모집중.toStudy();
        Study study2 = StudyFixture.자바_스터디_진행중.toStudy();

        assertAll(
                () -> assertThat(study1.calculateStudyPeriod()).isEqualTo(21),
                () -> assertThat(study2.calculateStudyPeriod()).isEqualTo(3)
        );
    }

    @Test
    void 스터디에_Member를_추가한다() {
        // given
        Study study = StudyFixture.자바_스터디_모집중.toStudy();
        Member member = MemberFixture.폰노이만.toMember();
        int sizeOfCurrentMembers = study.getCurrentRound().sizeOfCurrentMembers();

        // when
        study.addMember(member);
        int actual = study.getCurrentRound().sizeOfCurrentMembers();

        // then
        assertThat(actual).isEqualTo(sizeOfCurrentMembers + 1);
    }

    @Test
    void 모집중이지_않은_스터디에_Member를_추가하면_예외가_발생한다() {
        // given
        Study study = StudyFixture.자바_스터디_진행중.toStudy();
        Member member = MemberFixture.폰노이만.toMember();

        // when
        // then
        assertThatThrownBy(() -> study.addMember(member))
                .isInstanceOf(InvalidProcessingStatusException.class);
    }

    @Test
    void 정원이_가득_찬_스터디에_Member를_추가하면_예외가_발생한다() {
        // given
        Study study = StudyFixture.자바_스터디_모집중_정원_1.toStudy();
        Member member = MemberFixture.폰노이만.toMember();

        // when
        // then
        assertThatThrownBy(() -> study.addMember(member))
                .isInstanceOf(InvalidMemberSizeException.class);
    }
}
