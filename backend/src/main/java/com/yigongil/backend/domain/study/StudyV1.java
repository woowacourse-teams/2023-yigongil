package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.exception.CannotStartException;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@DiscriminatorValue(value = "V1")
@Entity(name = "study_v1")
public class StudyV1 extends Study {


    @Column(nullable = false)
    private Integer totalRoundCount;

    @Column(nullable = false)
    private Integer periodOfRound;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PeriodUnit periodUnit;

    @Column(nullable = false)
    private LocalDateTime startAt;

    protected StudyV1() {
    }

    @Builder
    public StudyV1(
            Long id,
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            ProcessingStatus processingStatus,
            LocalDateTime endAt,
            Integer currentRoundNumber,
            List<Round> rounds,
            Integer totalRoundCount,
            Integer periodOfRound,
            PeriodUnit periodUnit,
            LocalDateTime startAt
    ) {
        super(id, name, introduction, numberOfMaximumMembers, processingStatus, endAt, currentRoundNumber, rounds);
        this.totalRoundCount = totalRoundCount;
        this.periodOfRound = periodOfRound;
        this.periodUnit = periodUnit;
        this.startAt = startAt;
    }

    public static Study initializeStudyOf(
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            LocalDateTime startAt,
            Integer totalRoundCount,
            String periodOfRound,
            Member master
    ) {
        StudyV1 study = StudyV1.builder()
                               .name(name)
                               .numberOfMaximumMembers(numberOfMaximumMembers)
                               .startAt(startAt)
                               .totalRoundCount(totalRoundCount)
                               .periodOfRound(PeriodUnit.getPeriodNumber(periodOfRound))
                               .periodUnit(PeriodUnit.getPeriodUnit(periodOfRound))
                               .introduction(introduction)
                               .processingStatus(ProcessingStatus.RECRUITING)
                               .build();
        study.rounds = Round.of(totalRoundCount, master);
        return study;
    }

    @Override
    public boolean isCurrentRoundEndAt(LocalDate today) {
        return getCurrentRound().isEndAt(today);
    }

    @Override
    public void startStudy() {
        if (processingStatus != ProcessingStatus.RECRUITING) {
            throw new CannotStartException("시작할 수 없는 상태입니다.", id);
        }
        if (sizeOfCurrentMembers() == ONE_MEMBER) {
            throw new CannotStartException("시작할 수 없는 상태입니다.", id);
        }
        this.startAt = LocalDateTime.now();
        this.processingStatus = ProcessingStatus.PROCESSING;
        initializeRoundsEndAt();
    }

    private void initializeRoundsEndAt() {
        rounds.sort(Comparator.comparing(Round::getRoundNumber));
        LocalDateTime date = LocalDateTime.of(startAt.toLocalDate(), LocalTime.MIN);
        for (Round round : rounds) {
            date = date.plusDays(calculateStudyPeriod());
            round.updateEndAt(date);
        }
    }

    @Override
    public int calculateStudyPeriod() {
        return periodOfRound * periodUnit.getUnitNumber();
    }

    @Override
    public String findPeriodOfRoundToString() {
        return periodUnit.toStringFormat(periodOfRound);
    }

    @Override
    public void updateInformation(
            Member member,
            String name,
            Integer numberOfMaximumMembers,
            LocalDateTime startAt,
            Integer totalRoundCount,
            String periodOfRound,
            String introduction
    ) {
        validateMaster(member);
        validateStudyProcessingStatus();
        this.name = name;
        this.numberOfMaximumMembers = numberOfMaximumMembers;
        this.startAt = startAt;
        this.totalRoundCount = totalRoundCount;
        this.periodOfRound = PeriodUnit.getPeriodNumber(periodOfRound);
        this.periodUnit = PeriodUnit.getPeriodUnit(periodOfRound);
        this.introduction = introduction;
    }

    private void validateStudyProcessingStatus() {
        if (isNotRecruiting()) {
            throw new InvalidProcessingStatusException("현재 스터디의 상태가 모집중이 아닙니다.",
                    processingStatus.name());
        }
    }
}
