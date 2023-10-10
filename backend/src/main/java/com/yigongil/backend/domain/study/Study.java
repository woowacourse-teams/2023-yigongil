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
    private static final int INITIAL_ROUND_COUNT = 2;

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

    private LocalDateTime endAt;

    @Column(nullable = false)
    private Integer minimumWeeks;

    @Column(nullable = false)
    private Integer meetingDaysCountPerWeek;

    @Column(nullable = false)
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
            LocalDateTime endAt,
            Integer minimumWeeks,
            Integer meetingDaysCountPerWeek,
            Integer currentRoundNumber,
            List<Round> rounds
    ) {
        name = name.strip();
        validateNumberOfMaximumMembers(numberOfMaximumMembers);
        validateName(name);
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.numberOfMaximumMembers = numberOfMaximumMembers;
        this.processingStatus = processingStatus;
        this.endAt = endAt;
        this.currentRoundNumber = currentRoundNumber == null ? 1 : currentRoundNumber;
        this.rounds = rounds == null ? new ArrayList<>() : rounds;
        this.minimumWeeks = minimumWeeks;
        this.meetingDaysCountPerWeek = meetingDaysCountPerWeek;
    }

    public static Study initializeStudyOf(
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            Integer meetingDaysCountPerWeek,
            Integer minimumWeeks,
            Member master
    ) {
        Study study = Study.builder()
                           .name(name)
                           .numberOfMaximumMembers(numberOfMaximumMembers)
                           .meetingDaysCountPerWeek(meetingDaysCountPerWeek)
                           .minimumWeeks(minimumWeeks)
                           .introduction(introduction)
                           .processingStatus(ProcessingStatus.RECRUITING)
                           .build();
        study.rounds = Round.of(INITIAL_ROUND_COUNT, master);
        return study;
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

    public Integer calculateAverageTier() {
        return getCurrentRound().calculateAverageTier();
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
        return getCurrentRound().sizeOfCurrentMembers();
    }

    public void validateMaster(Member candidate) {
        getCurrentRound().validateMaster(candidate);
    }

    public boolean isCurrentRoundEndAt(LocalDate today) {
        return getCurrentRound().isEndAt(today);
    }

    public void updateToNextRound() {
        int nextRoundNumber = getCurrentRound().getRoundNumber() + 1;
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
    }

    public Member getMaster() {
        return getCurrentRound().getMaster();
    }

    protected void validateStudyProcessingStatus() {
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

    public void completeRound(Member member) {
        getCurrentRound().completeRound(member);
    }

    public List<RoundOfMember> getCurrentRoundOfMembers() {
        return getCurrentRound().getRoundOfMembers();
    }

    public int calculateSuccessfulRoundCount(Member member) {
        return (int) rounds.stream()
                           .filter(round -> round.isSuccess(member))
                           .count();
    }

    public Round getCurrentRound() {
        return rounds.stream()
                     .filter(round -> round.getRoundNumber().equals(currentRoundNumber))
                     .findAny()
                     .orElseThrow();
    }

    public boolean isMaster(Member member) {
        return getCurrentRound().isMaster(member);
    }

    public void updateInformation(
            Member member,
            String name,
            Integer numberOfMaximumMembers,
            String introduction,
            Integer minimumWeeks,
            Integer meetingDaysCountPerWeek
    ) {
        validateName(name);
        validateNumberOfMaximumMembers(numberOfMaximumMembers);
        validateMaster(member);
        validateStudyProcessingStatus();
        this.name = name;
        this.numberOfMaximumMembers = numberOfMaximumMembers;
        this.introduction = introduction;
        this.minimumWeeks = minimumWeeks;
        this.meetingDaysCountPerWeek = meetingDaysCountPerWeek;
    }

    public void startStudy() {
        if (processingStatus != ProcessingStatus.RECRUITING) {
            throw new CannotStartException("시작할 수 없는 상태입니다.", id);
        }
        if (sizeOfCurrentMembers() == ONE_MEMBER) {
            throw new CannotStartException("시작할 수 없는 상태입니다.", id);
        }
        this.processingStatus = ProcessingStatus.PROCESSING;
        initializeRoundsEndAt();
    }

    private void initializeRoundsEndAt() {
        rounds.sort(Comparator.comparing(Round::getRoundNumber));
        for (Round round : rounds) {
            // TODO: 10/5/23 진행하고자 하는 요일들로 초기화 필요
        }
    }
}
