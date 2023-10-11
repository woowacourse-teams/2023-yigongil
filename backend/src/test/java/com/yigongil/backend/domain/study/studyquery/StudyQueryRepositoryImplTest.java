package com.yigongil.backend.domain.study.studyquery;

import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.config.JpaConfig;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.domain.study.PageStrategy;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.fixture.StudyFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;

@Import(value = {JpaConfig.class, StudyQueryFactory.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class StudyQueryRepositoryImplTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.김진우.toMember());
    }

    @Test
    void 조건없는_전체조회를_검증한다() {
        for (int i = 0; i < 2; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions(null, ProcessingStatus.ALL, PageStrategy.defaultPageStrategy(0));

        assertThat(result).hasSize(2);
    }

    @Test
    void 페이징_사이즈를_검증한다() {
        for (int i = 0; i < 50; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions(null, ProcessingStatus.ALL, PageStrategy.defaultPageStrategy(0));

        assertThat(result).hasSize(30);
    }

    @Test
    void 페이징_2페이지를_검증한다() {
        for (int i = 0; i < 50; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions(null, ProcessingStatus.ALL, PageStrategy.defaultPageStrategy(1));

        assertThat(result).hasSize(20);
    }

    @Test
    void 검색결과가_없는_것을_검증한다() {
        for (int i = 0; i < 2; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions("모던", ProcessingStatus.ALL, PageStrategy.defaultPageStrategy(0));

        assertThat(result).isEmpty();
    }

    @Test
    void 검색기능을_검증한다() {
        for (int i = 0; i < 5; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions("자바", ProcessingStatus.ALL, PageStrategy.defaultPageStrategy(0));

        assertThat(result).hasSize(5);
    }

    @Test
    void 필터링_결과가_없는_것을_검증한다() {
        for (int i = 0; i < 5; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions(null, ProcessingStatus.END, PageStrategy.defaultPageStrategy(0));

        assertThat(result).isEmpty();
    }

    @Test
    void 모집중_필터링_결과를_검증한다() {
        for (int i = 0; i < 5; i++) {
            studyRepository.save(StudyFixture.자바_스터디_모집중_정원_2.toStudyWithMaster(member));
        }
        Slice<Study> result = studyRepository.findStudiesByConditions(null, ProcessingStatus.RECRUITING, PageStrategy.defaultPageStrategy(0));

        assertThat(result).hasSize(5);
    }
}
