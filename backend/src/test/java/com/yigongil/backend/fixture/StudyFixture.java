package com.yigongil.backend.fixture;

import com.yigongil.backend.domain.study.PeriodUnit;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;

import java.time.LocalDateTime;
import java.util.List;

public enum StudyFixture {

    자바_스터디_진행중(1L, LocalDateTime.now(), "자바", "스터디소개", 3, PeriodUnit.DAY, ProcessingStatus.PROCESSING, 3, 4),
    자바_스터디_모집중(1L, LocalDateTime.now(), "자바", "스터디소개", 3, PeriodUnit.WEEK, ProcessingStatus.RECRUITING, 3, 4),
    자바_스터디_모집중_정원_1(1L, LocalDateTime.now(), "자바", "스터디소개", 3, PeriodUnit.DAY, ProcessingStatus.RECRUITING, 3, 1),
    ;

    private final Long id;
    private final LocalDateTime startAt;
    private final String name;
    private final String introduction;
    private final Integer periodOfRound;
    private final PeriodUnit periodUnit;
    private final ProcessingStatus processingStatus;
    private final Integer totalRoundCount;
    private final Integer numberOfMaximumMember;

    StudyFixture(Long id, LocalDateTime startAt, String name, String introduction, Integer periodOfRound,
                 PeriodUnit periodUnit, ProcessingStatus processingStatus, Integer totalRoundCount, Integer numberOfMaximumMember) {
        this.id = id;
        this.startAt = startAt;
        this.name = name;
        this.introduction = introduction;
        this.periodOfRound = periodOfRound;
        this.periodUnit = periodUnit;
        this.processingStatus = processingStatus;
        this.totalRoundCount = totalRoundCount;
        this.numberOfMaximumMember = numberOfMaximumMember;
    }

    public Study toStudy() {
        Round round = RoundFixture.아이디_삼_투두없는_라운드.toRound();
        return Study.builder()
                .id(id)
                .startAt(startAt)
                .name(name)
                .introduction(introduction)
                .periodOfRound(periodOfRound)
                .processingStatus(processingStatus)
                .totalRoundCount(totalRoundCount)
                .periodUnit(periodUnit)
                .numberOfMaximumMembers(numberOfMaximumMember)
                .rounds(List.of(round))
                .currentRound(round)
                .build();
    }
}
