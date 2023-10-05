package com.yigongil.backend.fixture;

import static com.yigongil.backend.fixture.RoundFixture.아이디없는_라운드;
import static com.yigongil.backend.fixture.RoundFixture.아이디없는_라운드2;
import static com.yigongil.backend.fixture.RoundFixture.아이디없는_라운드3;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum StudyFixture {

    자바_스터디_진행중(1L, LocalDateTime.now(), "자바", "스터디소개", ProcessingStatus.PROCESSING, 4),
    자바_스터디_모집중(1L, LocalDateTime.now(), "자바", "스터디소개", ProcessingStatus.RECRUITING, 4),
    자바_스터디_모집중_정원_2(1L, LocalDateTime.now(), "자바", "스터디소개", ProcessingStatus.RECRUITING, 2),
    ;

    private final Long id;
    private final LocalDateTime startAt;
    private final String name;
    private final String introduction;
    private final ProcessingStatus processingStatus;
    private final Integer numberOfMaximumMember;

    StudyFixture(Long id, LocalDateTime startAt, String name, String introduction,
                 ProcessingStatus processingStatus, Integer numberOfMaximumMember) {
        this.id = id;
        this.startAt = startAt;
        this.name = name;
        this.introduction = introduction;
        this.processingStatus = processingStatus;
        this.numberOfMaximumMember = numberOfMaximumMember;
    }

    public Study toStudy() {
        return Study.builder()
                    .id(id)
                    .startAt(startAt)
                    .name(name)
                    .introduction(introduction)
                    .processingStatus(processingStatus)
                    .numberOfMaximumMembers(numberOfMaximumMember)
                    .rounds(List.of(아이디없는_라운드.toRound(), 아이디없는_라운드2.toRound(), 아이디없는_라운드3.toRound()))
                    .build();
    }

    public Study toStudyWithRounds(RoundFixture... roundFixtures) {
        List<Round> rounds = new ArrayList<>(Arrays.stream(roundFixtures)
                                                   .map(RoundFixture::toRound)
                                                   .toList());

        return Study.builder()
                    .id(id)
                    .startAt(startAt)
                    .name(name)
                    .introduction(introduction)
                    .processingStatus(processingStatus)
                    .numberOfMaximumMembers(numberOfMaximumMember)
                    .rounds(rounds)
                    .build();
    }
}
