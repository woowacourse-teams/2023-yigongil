package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.exception.InvalidPeriodUnitException;
import com.yigongil.backend.utils.DateConverter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Builder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Study extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 200)
    private String introduction;

    @Column(nullable = false)
    private Integer numberOfMaximumMembers;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProcessingStatus processingStatus;

    @Column(nullable = false)
    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Column(nullable = false)
    private Integer totalRoundCount;

    @Column(nullable = false)
    private Integer periodOfRound;

    @OneToOne(fetch = FetchType.LAZY)
    private Round currentRound;

    @Cascade(CascadeType.PERSIST)
    @OneToMany
    @JoinColumn(name = "study_id", nullable = false)
    private List<Round> rounds = new ArrayList<>();

    protected Study() {
    }

    @Builder
    public Study(
            Long id,
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            ProcessingStatus processingStatus,
            LocalDateTime startAt,
            LocalDateTime endAt,
            Integer totalRoundCount,
            Integer periodOfRound,
            Round currentRound,
            List<Round> rounds
    ) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.numberOfMaximumMembers = numberOfMaximumMembers;
        this.processingStatus = processingStatus;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalRoundCount = totalRoundCount;
        this.periodOfRound = periodOfRound;
        this.currentRound = currentRound;
        this.rounds = rounds;
    }

    public static Study initializeStudyOf(
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            String startAt,
            Integer totalRoundCount,
            String periodOfRound,
            Member master
    ) {
        Study study = Study.builder()
                .name(name)
                .numberOfMaximumMembers(numberOfMaximumMembers)
                .startAt(DateConverter.toLocalDateTime(startAt))
                .totalRoundCount(totalRoundCount)
                .periodOfRound(convertRoundStringToDays(periodOfRound))
                .introduction(introduction)
                .processingStatus(ProcessingStatus.RECRUITING)
                .build();
        study.rounds = Round.of(totalRoundCount, master);
        study.currentRound = study.rounds.get(0);
        return study;
    }

    private static int convertRoundStringToDays(String periodOfRound) {
        int numericPart = Integer.parseInt(periodOfRound.substring(0, periodOfRound.length() - 1));
        if (periodOfRound.toLowerCase().endsWith("d")) {
            return numericPart;
        }
        if (periodOfRound.toLowerCase().endsWith("w")) {
            return numericPart * 7;
        }
        throw new InvalidPeriodUnitException("스터디 주기는 일 또는 주 로만 설정할 수 있습니다.", periodOfRound);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Integer getNumberOfMaximumMembers() {
        return numberOfMaximumMembers;
    }

    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public Integer getTotalRoundCount() {
        return totalRoundCount;
    }

    public Integer getPeriodOfRound() {
        return periodOfRound;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public List<Round> getRounds() {
        return rounds;
    }
}
