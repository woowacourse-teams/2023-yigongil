package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.roundofmember.RoundOfMembers;
import com.yigongil.backend.exception.CannotStartException;
import com.yigongil.backend.domain.studymember.Role;
import com.yigongil.backend.exception.InvalidMemberSizeException;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.utils.DateConverter;
import lombok.Builder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Study extends BaseEntity {

    private static final int ONE_MEMBER = 1;
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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PeriodUnit periodUnit;

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
            List<Round> rounds,
            PeriodUnit periodUnit
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
        this.periodUnit = periodUnit;
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
                .periodOfRound(PeriodUnit.getPeriodNumber(periodOfRound))
                .periodUnit(PeriodUnit.getPeriodUnit(periodOfRound))
                .introduction(introduction)
                .processingStatus(ProcessingStatus.RECRUITING)
                .build();
        study.rounds = Round.of(totalRoundCount, master);
        study.currentRound = study.rounds.get(0);
        return study;
    }

    public Integer calculateAverageTier() {
        return currentRound.calculateAverageTier();
    }

    public Long createNecessaryTodo(Member author, Long roundId, String content) {
        Round targetRound = findRoundById(roundId);
        targetRound.createNecessaryTodo(author, content);
        return targetRound.getId();
    }

    public OptionalTodo createOptionalTodo(Member author, Long roundId, String content) {
        Round targetRound = findRoundById(roundId);
        return targetRound.createOptionalTodo(author, content);
    }

    public void updateNecessaryTodoContent(Long todoId, String content) {
        Round round = findRoundById(todoId);
        round.updateNecessaryTodoContent(content);
    }

    public void updateNecessaryTodoIsDone(Member author, Long todoId, Boolean isDone) {
        Round round = findRoundById(todoId);
        RoundOfMember roundOfMemberBy = round.findRoundOfMemberBy(author);
        roundOfMemberBy.updateNecessaryTodoIsDone(isDone);
    }

    public Round findRoundById(Long roundId) {
        return rounds.stream()
                .filter(round -> round.getId().equals(roundId))
                .findAny()
                .orElseThrow(() -> new RoundNotFoundException("스터디에 해당 회차가 존재하지 않습니다.", roundId));
    }

    public void addMember(Member member) {
        validateStudyProcessingStatus();
        validateMemberSize();
        this.currentRound.addMember(member);
    }

    private void validateStudyProcessingStatus() {
        if (!isRecruiting()) {
            throw new InvalidProcessingStatusException("모집 중인 스터디가 아니기 때문에 신청을 수락할 수 없습니다.", processingStatus.name());
        }
    }

    public boolean isRecruiting() {
        return this.processingStatus == ProcessingStatus.RECRUITING;
    }

    private void validateMemberSize() {
        if (sizeOfCurrentMembers() >= numberOfMaximumMembers) {
            throw new InvalidMemberSizeException("스터디 정원이 가득 찼습니다.", numberOfMaximumMembers);
        }
    }

    private int sizeOfCurrentMembers() {
        return currentRound.sizeOfCurrentMembers();
    }

    public void validateMaster(Member candidate) {
        currentRound.validateMaster(candidate);
    }

    public RoundOfMember findCurrentRoundOfMemberBy(Member member) {
        return currentRound.findRoundOfMemberBy(member);
    }

    public RoundOfMembers findCurrentRoundOfMembers() {
        return new RoundOfMembers(currentRound.getRoundOfMembers());
    }

    public boolean isCurrentRoundEndAt(LocalDate today) {
        return currentRound.isEndAt(today);
    }

    public void updateToNextRound() {
        final int nextRoundNumber = currentRound.getRoundNumber() + 1;
        final Optional<Round> nextRound = rounds.stream()
                                                .filter(round -> round.getRoundNumber() == nextRoundNumber)
                                                .findFirst();

        if (nextRound.isPresent()) {
            this.currentRound = nextRound.get();
            return;
        }

        this.processingStatus = ProcessingStatus.END;
    }

    public void startStudy() {
        if (processingStatus != ProcessingStatus.RECRUITING) {
            throw new CannotStartException("시작할 수 없는 상태입니다.", id);
        }
        if (sizeOfCurrentMembers() == ONE_MEMBER) {
            throw new CannotStartException("시작할 수 없는 상태입니다.", id);
        }
        this.startAt = LocalDateTime.now();
        this.processingStatus = ProcessingStatus.PROCESSING;
    }

    public int calculateStudyPeriod() {
        return periodOfRound * periodUnit.getUnitNumber();
    }

    public Member getMaster() {
        return currentRound.getMaster();
    }

    public String findPeriodOfRoundToString() {
        return periodUnit.toStringFormat(periodOfRound);
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

    public PeriodUnit getPeriodUnit() {
        return periodUnit;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public List<Round> getRounds() {
        return rounds;
    }
}
