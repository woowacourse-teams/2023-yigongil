package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.round.Round;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.yigongil.backend.fixture.StudyFixture.자바_스터디;
import static org.assertj.core.api.Assertions.assertThat;

class StudyTest {

    @Test
    void 스터디_주기를_계산한다() {
        Study study1 = StudyFixture.자바_스터디_모집중.toStudy();
        Study study2 = StudyFixture.자바_스터디.toStudy();

        assertAll(
                () -> assertThat(study1.calculateStudyPeriod()).isEqualTo(21),
                () -> assertThat(study2.calculateStudyPeriod()).isEqualTo(3)
        );
    void 스터디를_다음_라운드로_넘기면_현재_라운드가_다음_라운드로_변한다() {
        // given
        Study study = 자바_스터디.toStudy();
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
        Study study = 자바_스터디.toStudy();
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
        Study study = 자바_스터디.toStudy();
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
        Study study = 자바_스터디.toStudy();
        Round currentRound = study.getCurrentRound();
        LocalDateTime endAt = currentRound.getEndAt();

        // when
        boolean actual = study.isCurrentRoundEndAt(endAt.plusDays(1).toLocalDate());

        // then
        assertThat(actual).isFalse();
    }
}
