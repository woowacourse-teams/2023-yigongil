package com.yigongil.backend.domain.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.exception.InvalidMemberSizeException;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.StudyFixture;

import java.time.LocalDateTime;
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
    void 스터디를_다음_라운드로_넘기면_현재_라운드가_다음_라운드로_변한다() {
        // given
        Study study = StudyFixture.자바_스터디_진행중.toStudy();
        Round currentRound = study.getCurrentRound();

        // when
        study.updateToNextRound();
        Round nextRound = study.getCurrentRound();

        // then
        assertThat(nextRound.getRoundNumber()).isEqualTo(currentRound.getRoundNumber() + 1);
    }

    @Test
    void 마지막_라운드에서_라운드를_넘기면_스터디가_종료된다() {
        // given
        Study study = StudyFixture.자바_스터디_모집중.toStudy();
        study.updateToNextRound();
        study.updateToNextRound();

        // when
        study.updateToNextRound();

        // then
        assertThat(study.getProcessingStatus()).isSameAs(ProcessingStatus.END);
    }

    @Test
    void 스터디의_현재_라운드가_종료되는_날이면_true를_반환한다() {
        // given
        Study study = StudyFixture.자바_스터디_진행중.toStudy();
        Round currentRound = study.getCurrentRound();
        LocalDateTime endAt = currentRound.getEndAt();

        // when
        boolean actual = study.isCurrentRoundEndAt(endAt.toLocalDate());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 스터디의_현재_라운드가_종료되는_날이_아니면_false를_반환한다() {
        // given
        Study study = StudyFixture.자바_스터디_진행중.toStudy();
        Round currentRound = study.getCurrentRound();
        LocalDateTime endAt = currentRound.getEndAt();

        // when
        boolean actual = study.isCurrentRoundEndAt(endAt.plusDays(1).toLocalDate());

        // then
        assertThat(actual).isFalse();
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
