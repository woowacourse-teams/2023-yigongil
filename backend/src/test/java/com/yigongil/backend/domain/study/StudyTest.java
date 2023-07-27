package com.yigongil.backend.domain.study;

import com.yigongil.backend.fixture.StudyFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class StudyTest {

    @Test
    void 스터디_주기를_계산한다() {
        Study study1 = StudyFixture.자바_스터디_모집중.toStudy();
        Study study2 = StudyFixture.자바_스터디.toStudy();

        assertAll(
                () -> assertThat(study1.calculateStudyPeriod()).isEqualTo(21),
                () -> assertThat(study2.calculateStudyPeriod()).isEqualTo(3)
        );
    }
}
