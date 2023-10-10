package com.yigongil.backend.domain.study.studyquery;

import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.domain.study.ProcessingStatus;
import org.junit.jupiter.api.Test;

class StudyQueryFactoryTest {

    private static final StudyQueryFactory studyQueryFactory = new StudyQueryFactory();

    @Test
    void 조건이_하나인_쿼리_생성() {
        String java = studyQueryFactory.builder()
                                       .search("java")
                                       .sort("id desc")
                                       .build();

        assertThat(java).isEqualTo("select s from Study s where s.name like '%java%' order by s.id desc ");
    }

    @Test
    void 조건이_두개인_쿼리_생성() {
        String java = studyQueryFactory.builder()
                                       .status(ProcessingStatus.PROCESSING)
                                       .search("java")
                                       .sort("id desc")
                                       .build();

        assertThat(java).isEqualTo("select s from Study s where s.processingStatus = 'PROCESSING' and s.name like '%java%' order by s.id desc ");
    }

    @Test
    void 조건이_두개인_쿼리_생성_순서를_바꿔서_검증() {
        String java = studyQueryFactory.builder()
                                       .search("java")
                                       .status(ProcessingStatus.PROCESSING)
                                       .sort("id desc")
                                       .build();

        assertThat(java).isEqualTo("select s from Study s where s.name like '%java%' and s.processingStatus = 'PROCESSING' order by s.id desc ");
    }

    @Test
    void 조건이_없는_쿼리_생성() {
        String java = studyQueryFactory.builder()
                                       .sort("id desc")
                                       .build();

        assertThat(java).isEqualTo("select s from Study s order by s.id desc ");
    }

    @Test
    void 정렬_조건이_없는_쿼리_생성() {
        String java = studyQueryFactory.builder()
                                       .build();

        assertThat(java).isEqualTo("select s from Study s ");
    }
}
