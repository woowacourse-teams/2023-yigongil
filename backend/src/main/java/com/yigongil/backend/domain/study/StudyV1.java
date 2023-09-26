package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@DiscriminatorValue("1")
public class StudyV1 extends Study {

    @Column(nullable = false)
    private Integer totalRoundCount;

    @Column(nullable = false)
    private Integer periodOfRound;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PeriodUnit periodUnit;

    @Column(nullable = false)
    private LocalDateTime expectedStartAt;

    @Builder
    public StudyV1(
        Long id,
        String name,
        String introduction,
        Integer numberOfMaximumMembers,
        ProcessingStatus processingStatus,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer totalRoundCount,
        Integer periodOfRound,
        Integer currentRoundNumber,
        List<Round> rounds,
        PeriodUnit periodUnit,
        LocalDateTime expectedStartAt
    ) {
        super(
            id,
            name,
            introduction,
            numberOfMaximumMembers,
            processingStatus,
            startAt,
            endAt,
            currentRoundNumber,
            rounds
        );
        this.totalRoundCount = totalRoundCount;
        this.periodOfRound = periodOfRound;
        this.periodUnit = periodUnit;
        this.expectedStartAt = expectedStartAt;
    }


    protected StudyV1() {

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
        Study study = StudyV1.builder()
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

    public String findPeriodOfRoundToString() {
        return periodUnit.toStringFormat(periodOfRound);
    }

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
        super.updateInformation(name, numberOfMaximumMembers, startAt, introduction);
        this.totalRoundCount = totalRoundCount;
        this.periodOfRound = PeriodUnit.getPeriodNumber(periodOfRound);
        this.periodUnit = PeriodUnit.getPeriodUnit(periodOfRound);
    }
}
