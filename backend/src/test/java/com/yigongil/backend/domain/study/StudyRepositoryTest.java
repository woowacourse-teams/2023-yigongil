package com.yigongil.backend.domain.study;

import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.config.JpaConfig;
import com.yigongil.backend.fixture.StudyFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    StudyRepository studyRepository;

    @Test
    void 레퍼지토리_타입_테스트() {
        Study save = studyRepository.save(StudyFixture.자바_스터디_모집중.toStudy());

        Study study = studyRepository.findById(save.getId()).get();

        assertThat(study).isInstanceOf(StudyV1.class);
    }

    @Test
    void 레퍼지토리_타입_테스트2() {
        Study save = studyRepository.save(StudyFixture.자바_스터디_모집중.toStudy());

        Study study = studyRepository.findById(save.getId()).get();

        assertThat(study).isNotInstanceOf(StudyV2.class);
    }
}
