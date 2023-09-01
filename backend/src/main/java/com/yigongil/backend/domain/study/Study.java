package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.exception.CannotStartException;
import com.yigongil.backend.exception.InvalidMemberSizeException;
import com.yigongil.backend.exception.InvalidNumberOfMaximumStudyMember;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.exception.InvalidStudyNameLengthException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class Study extends BaseEntity {

    private static final int ONE_MEMBER = 1;
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 30;
    private static final int MIN_MEMBER_SIZE = 2;
    private static final int MAX_MEMBER_SIZE = 8;

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

    private Integer currentRoundNumber;

    @Cascade(CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
            Integer currentRoundNumber,
            List<Round> rounds,
            PeriodUnit periodUnit
    ) {
        name = name.strip();
        validateNumberOfMaximumMembers(numberOfMaximumMembers);
        validateName(name);
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
        this.currentRoundNumber = currentRoundNumber == null ? 1 : currentRoundNumber;
        this.rounds = rounds == null ? new ArrayList<>() : rounds;
    }

    private void validateNumberOfMaximumMembers(Integer numberOfMaximumMembers) {
        if (numberOfMaximumMembers < MIN_MEMBER_SIZE || numberOfMaximumMembers > MAX_MEMBER_SIZE) {
            throw new InvalidNumberOfMaximumStudyMember(
                    String.format(
                            "스터디의 정원은 최소 %d명 최대 %d명까지 설정 가능합니다",
                            MIN_MEMBER_SIZE,
                            MAX_MEMBER_SIZE
                    ), numberOfMaximumMembers
            );
        }
    }

    private void validateName(String name) {
        int nameLength = name.length();
        if (nameLength < MIN_NAME_LENGTH || nameLength > MAX_NAME_LENGTH) {
            throw new InvalidStudyNameLengthException(
                    String.format(
                            "스터디 이름의 길이는 %d자 이상 %d자 이하로 작성 가능합니다.",
                            MIN_NAME_LENGTH,
                            MAX_NAME_LENGTH
                    ), nameLength
            );
        }
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
        Study study = Study.builder()
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

    public Integer calculateAverageTier() {
        return currentRound().calculateAverageTier();
    }

    public void addMember(Member member) {
        validateStudyProcessingStatus();
        validateMemberSize();

        for (Round round : rounds) {
            round.addMember(member);
        }
    }

    public void validateMemberSize() {
        if (sizeOfCurrentMembers() >= numberOfMaximumMembers) {
            throw new InvalidMemberSizeException("스터디 정원이 가득 찼습니다.", numberOfMaximumMembers);
        }
    }

    public int sizeOfCurrentMembers() {
        return currentRound().sizeOfCurrentMembers();
    }

    public void validateMaster(Member candidate) {
        currentRound().validateMaster(candidate);
    }

    public RoundOfMember findCurrentRoundOfMemberBy(Member member) {
        return currentRound().findRoundOfMemberBy(member);
    }

    public boolean isCurrentRoundEndAt(LocalDate today) {
        return currentRound().isEndAt(today);
    }

    public void updateToNextRound() {
        int nextRoundNumber = currentRound().getRoundNumber() + 1;
        Optional<Round> nextRound = rounds.stream()
                                          .filter(round -> round.getRoundNumber() == nextRoundNumber)
                                          .findFirst();

        nextRound.ifPresentOrElse(this::updateCurrentRound, this::finishStudy);
    }

    private void updateCurrentRound(Round upcomingRound) {
        this.currentRoundNumber++;
    }

    private void finishStudy() {
        this.processingStatus = ProcessingStatus.END;
        currentRound().updateMembersTier();
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

    public int calculateStudyPeriod() {
        return periodOfRound * periodUnit.getUnitNumber();
    }

    public String findPeriodOfRoundToString() {
        return periodUnit.toStringFormat(periodOfRound);
    }

    public Member getMaster() {
        return currentRound().getMaster();
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
        this.name = name;
        this.numberOfMaximumMembers = numberOfMaximumMembers;
        this.startAt = startAt;
        this.totalRoundCount = totalRoundCount;
        this.periodOfRound = PeriodUnit.getPeriodNumber(periodOfRound);
        this.periodUnit = PeriodUnit.getPeriodUnit(periodOfRound);
        this.introduction = introduction;
    }

    private void validateStudyProcessingStatus() {
        if (!isRecruiting()) {
            throw new InvalidProcessingStatusException("현재 스터디의 상태가 모집중이 아닙니다.",
                    processingStatus.name());
        }
    }

    public boolean isRecruiting() {
        return this.processingStatus == ProcessingStatus.RECRUITING;
    }

    public boolean isEnd() {
        return this.processingStatus == ProcessingStatus.END;
    }

    public Round currentRound() {
        return rounds.stream()
                     .filter(round -> round.getRoundNumber().equals(currentRoundNumber))
                     .findAny()
                     .orElseThrow();
    }

    public void removeAllRoundMemberByMemberId(Long memberId) {
        for (Round round : rounds) {
            round.removeRoundOfMemberByMemberId(memberId);
        }
    }
}
