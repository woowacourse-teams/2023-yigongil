package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Getter;

@Getter
@DiscriminatorValue(value = "V2")
@Entity(name = "study_v2")
public class StudyV2 extends Study {

    private Integer roundsPerWeek;
    private Integer minimumWeek;

    protected StudyV2() {
    }

    @Override
    public void startStudy() {

    }

    @Override
    public int calculateStudyPeriod() {
        return 0;
    }

    @Override
    public String findPeriodOfRoundToString() {
        return null;
    }

    @Override
    public void updateInformation(Member member, String name, Integer numberOfMaximumMembers, LocalDateTime startAt, Integer totalRoundCount, String periodOfRound, String introduction) {

    }

    public StudyV2(
            Long id,
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            ProcessingStatus processingStatus,
            LocalDateTime endAt,
            Integer currentRoundNumber,
            List<Round> rounds,
            Integer roundsPerWeek,
            Integer minimumWeek
    ) {
        super(id, name, introduction, numberOfMaximumMembers, processingStatus, endAt, currentRoundNumber, rounds);
        this.roundsPerWeek = roundsPerWeek;
        this.minimumWeek = minimumWeek;
    }
}
