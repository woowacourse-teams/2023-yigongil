package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.meetingdayoftheweek.MeetingDayOfTheWeek;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.studymember.Role;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyResult;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.exception.CannotEndException;
import com.yigongil.backend.exception.CannotStartException;
import com.yigongil.backend.exception.InvalidMemberSizeException;
import com.yigongil.backend.exception.InvalidNumberOfMaximumStudyMember;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.exception.InvalidStudyNameLengthException;
import com.yigongil.backend.exception.NotStudyMasterException;
import com.yigongil.backend.exception.NotStudyMemberException;
import com.yigongil.backend.exception.RoundNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private static final int FIRST_WEEK = 1;

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

    @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "study", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MeetingDayOfTheWeek> meetingDaysOfTheWeek = new ArrayList<>();

    @Column(nullable = false)
    private Integer minimumWeeks;

    @Column(nullable = false)
    private Integer meetingDaysCountPerWeek;

    @Column
    private Long currentRoundNumber;

    @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "study", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyMember> studyMembers = new ArrayList<>();


    @Cascade(CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "study", orphanRemoval = true, fetch = FetchType.LAZY)
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
            Member master,
            Integer minimumWeeks,
            Integer meetingDaysCountPerWeek,
            Long currentRoundNumber,
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
        this.currentRoundNumber = currentRoundNumber;
        this.studyMembers.add(StudyMember.builder()
                                         .study(this)
                                         .role(Role.MASTER)
                                         .member(master)
                                         .studyResult(StudyResult.NONE)
                                         .build());
        this.rounds = rounds == null ? new ArrayList<>() : rounds;
        this.minimumWeeks = minimumWeeks;
        this.meetingDaysCountPerWeek = meetingDaysCountPerWeek;
    }

    public static Study initializeStudyOf(
            String name,
            String introduction,
            Integer numberOfMaximumMembers,
            Integer minimumWeeks,
            Integer meetingDaysCountPerWeek,
            Member master
    ) {
        return Study.builder()
                    .name(name)
                    .numberOfMaximumMembers(numberOfMaximumMembers)
                    .meetingDaysCountPerWeek(meetingDaysCountPerWeek)
                    .minimumWeeks(minimumWeeks)
                    .introduction(introduction)
                    .processingStatus(ProcessingStatus.RECRUITING)
                    .master(master)
                    .build();
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
        return (int) studyMembers.stream()
                                 .filter(StudyMember::isStudyMember)
                                 .map(StudyMember::getMember)
                                 .mapToInt(member -> member.getTier().getOrder())
                                 .average()
                                 .orElseThrow();
    }

    public void permit(Member applicant, Member master) {
        validateStudyProcessingStatus();
        validateMaster(master);

        studyMembers.stream()
                    .filter(studyMember -> studyMember.getMember().equals(applicant))
                    .findAny()
                    .ifPresent(StudyMember::participate);
    }

    public void validateMemberSize() {
        if (sizeOfCurrentMembers() >= numberOfMaximumMembers) {
            throw new InvalidMemberSizeException("스터디 정원이 가득 찼습니다.", numberOfMaximumMembers);
        }
    }

    public int sizeOfCurrentMembers() {
        return (int) studyMembers.stream()
                                 .filter(StudyMember::isStudyMember)
                                 .count();
    }

    public boolean isCurrentRoundEndAt(LocalDate today) {
        return getCurrentRound().isEndAt(today);
    }

    public void start(Member member, List<DayOfWeek> daysOfTheWeek, LocalDateTime startAt) {
        validateMaster(member);
        if (processingStatus != ProcessingStatus.RECRUITING) {
            throw new CannotStartException("이미 진행중이거나 종료된 스터디입니다.", id);
        }
        if (sizeOfCurrentMembers() == ONE_MEMBER) {
            throw new CannotStartException("스터디의 멤버가 한 명일 때는 시작할 수 없습니다.", id);
        }
        deleteLeftApplicant();

        this.processingStatus = ProcessingStatus.PROCESSING;
        initializeMeetingDaysOfTheWeek(daysOfTheWeek);
        initializeRounds(startAt.toLocalDate());
    }

    private void deleteLeftApplicant() {
        List<StudyMember> currentApplicant = studyMembers.stream()
                                                         .filter(StudyMember::isApplicant)
                                                         .toList();
        studyMembers.removeAll(currentApplicant);
    }

    private void initializeMeetingDaysOfTheWeek(List<DayOfWeek> daysOfTheWeek) {
        this.meetingDaysOfTheWeek.addAll(daysOfTheWeek.stream()
                                                      .map(dayOfTheWeek -> MeetingDayOfTheWeek.builder()
                                                                                              .dayOfWeek(dayOfTheWeek)
                                                                                              .study(this)
                                                                                              .build())
                                                      .toList());
    }

    private void initializeRounds(LocalDate startAt) {
        this.rounds.addAll(createRoundsOfFirstWeek(startAt));
        this.rounds.addAll(createRoundsOf(FIRST_WEEK + 1));
        rounds.get(0).proceed();
    }

    private List<Round> createRoundsOfFirstWeek(final LocalDate startAt) {
        List<Round> rounds = meetingDaysOfTheWeek.stream()
                                                 .filter(meetingDayOfTheWeek -> meetingDayOfTheWeek.comesNext(startAt.getDayOfWeek()))
                                                 .map(meetingDayOfTheWeek -> Round.of(meetingDayOfTheWeek, this, FIRST_WEEK))
                                                 .toList();
        if (rounds.isEmpty()) {
            rounds = createRoundsOf(FIRST_WEEK);
        }
        return rounds;
    }

    public void validateMaster(Member candidate) {
        Member master = getMaster();
        if (master.getId().equals(candidate.getId())) {
            return;
        }
        throw new NotStudyMasterException(" 머스트두를 수정할 권한이 없습니다.", candidate.getNickname());
    }

    public void updateToNextRound() {
        Round nextRound = findUpcomingRoundOf(getCurrentRound().getWeekNumber());

        if (nextRound.isSameWeek(getCurrentRound().getWeekNumber() + 1)) {
            rounds.addAll(createRoundsOf(nextRound.getWeekNumber() + 1));
        }
        updateCurrentRound(nextRound);
    }

    private void updateCurrentRound(Round upcomingRound) {
        upcomingRound.proceed();
        getCurrentRound().finish();
    }

    private Round findUpcomingRoundOf(int weekNumber) {
        return rounds.stream()
                     .filter(round -> round.isSameWeek(weekNumber) && round.isNextDayOfWeek(getCurrentRound().getDayOfWeek()))
                     .findFirst()
                     .orElseGet(() -> findFirstRoundOf(weekNumber + 1));
    }

    private Round findFirstRoundOf(int nextWeekNumber) {
        return rounds.stream()
                     .filter(round -> round.isSameWeek(nextWeekNumber) && round.isSameDayOfWeek(findFirstMeetingDayOfTheWeek()))
                     .findAny()
                     .orElseThrow(() -> new RoundNotFoundException("다음 주차의 라운드가 존재하지 않습니다.", getCurrentRound().getWeekNumber()));
    }

    private MeetingDayOfTheWeek findFirstMeetingDayOfTheWeek() {
        return meetingDaysOfTheWeek.stream()
                                   .min(Comparator.comparing(MeetingDayOfTheWeek::getOrder))
                                   .orElseThrow();
    }

    public void finishStudy(Member master) {
        validateMaster(master);
        validateStudyCanFinish();
        studyMembers.forEach(StudyMember::completeSuccessfully);
        this.processingStatus = ProcessingStatus.END;
    }

    private void validateStudyCanFinish() {
        if (isEnd()) {
            throw new CannotEndException("이미 종료된 스터디입니다.", String.valueOf(id));
        }
        if (getCurrentRound().isBeforeOrSame(minimumWeeks)) {
            throw new CannotEndException("최소 진행 주차를 채우지 못했습니다.", String.valueOf(id));
        }
    }

    public Member getMaster() {
        return studyMembers.stream()
                           .filter(StudyMember::isMaster)
                           .map(StudyMember::getMember)
                           .findAny()
                           .orElseThrow();
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
                     .filter(Round::isInProgress)
                     .findAny()
                     .orElseThrow(() -> new RoundNotFoundException("현재 진행중인 라운드가 없습니다.", -1));
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

    private List<Round> createRoundsOf(Integer weekNumber) {
        return meetingDaysOfTheWeek.stream()
                                   .map(meetingDayOfTheWeek -> Round.of(meetingDayOfTheWeek, this, weekNumber))
                                   .toList();
    }

    public void apply(Member member) {
        validateMemberSize();
        validateApplicant(member);
        studyMembers.add(StudyMember.builder()
                                    .study(this)
                                    .role(Role.APPLICANT)
                                    .member(member)
                                    .studyResult(StudyResult.NONE)
                                    .build());
    }

    private void validateApplicant(Member member) {
        if (isAlreadyExist(member)) {
            throw new ApplicantAlreadyExistException("스터디에 신청할 수 없는 멤버입니다.", String.valueOf(member.getId()));
        }
    }

    private boolean isAlreadyExist(Member member) {
        return studyMembers.stream()
                           .anyMatch(studyMember -> studyMember.getMember().equals(member));
    }

    public void exit(Member member) {
        validateCanExitMemberSize();
        rounds.forEach(round -> round.exit(member));
        findStudyMemberBy(member).exit();
    }

    private void validateCanExitMemberSize() {
        long studyMemberCount = studyMembers.stream()
                                            .filter(StudyMember::isStudyMember)
                                            .count();
        if (studyMemberCount <= MIN_MEMBER_SIZE) {
            throw new InvalidMemberSizeException("스터디 멤버 수가 2명 이하일 때는 탈퇴할 수 없습니다.",
                    (int) studyMemberCount);
        }
    }

    private StudyMember findStudyMemberBy(Member member) {
        return studyMembers.stream()
                           .filter(studyMember -> studyMember.getMember().equals(member))
                           .findAny()
                           .orElseThrow(() -> new NotStudyMemberException("해당 스터디의 멤버가 아닙니다.", member.getGithubId()));
    }
}
