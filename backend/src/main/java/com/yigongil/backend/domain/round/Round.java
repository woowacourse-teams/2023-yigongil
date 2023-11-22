package com.yigongil.backend.domain.round;

import static java.util.stream.Collectors.toMap;

import com.yigongil.backend.domain.base.BaseEntity;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.exception.NecessaryTodoNotExistException;
import com.yigongil.backend.exception.NotStudyMasterException;
import com.yigongil.backend.exception.NotStudyMemberException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class Round extends BaseEntity {

    private static final int MAX_TODO_CONTENT_LENGTH = 20;
    private static final int MIN_TODO_CONTENT_LENGTH = 1;

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "sequence_generator")
    @TableGenerator(name = "sequence_generator", table = "round_sequence",
        pkColumnName = "sequence_name", pkColumnValue = "id",
        initialValue = 1, allocationSize=500)
    @Id
    private Long id;

    @Column(name = "study_id")
    private Long studyId;

    @Column(length = MAX_TODO_CONTENT_LENGTH)
    private String mustDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member master;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany
    @JoinColumn(name = "round_id", nullable = false, updatable = false)
    private List<RoundOfMember> roundOfMembers = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private Integer weekNumber;

    @Enumerated(EnumType.STRING)
    private RoundStatus roundStatus = RoundStatus.NOT_STARTED;

    protected Round() {
    }

    @Builder
    public Round(
            Long id,
            Long studyId,
            String mustDo,
            Member master,
            List<RoundOfMember> roundOfMembers,
            DayOfWeek dayOfWeek,
            Integer weekNumber
    ) {
        this.id = id;
        this.studyId = studyId;
        this.mustDo = mustDo;
        this.master = master;
        this.roundOfMembers = roundOfMembers == null ? new ArrayList<>() : roundOfMembers;
        this.dayOfWeek = dayOfWeek;
        this.weekNumber = weekNumber;
    }

    public static Round of(DayOfWeek dayOfWeek, Study study, Integer weekNumber) {
        return Round.builder()
                    .studyId(study.getId())
                    .dayOfWeek(dayOfWeek)
                    .weekNumber(weekNumber)
                    .master(study.getMaster())
                    .roundOfMembers(RoundOfMember.from(study))
                    .build();
    }

    public void updateMustDo(Member author, String content) {
        validateTodoLength(content);
        validateMaster(author);
        mustDo = content;
    }

    public void validateMaster(Member member) {
        if (master.getId().equals(member.getId())) {
            return;
        }
        throw new NotStudyMasterException(" 머스트두를 수정할 권한이 없습니다.", member.getNickname());
    }

    private void validateTodoLength(String content) {
        int contentLength = content.length();
        if (contentLength > MAX_TODO_CONTENT_LENGTH || contentLength < MIN_TODO_CONTENT_LENGTH) {
            throw new InvalidTodoLengthException(
                    String.format(
                            "머스트두 길이는 %d자 이상 %d자 이하로 작성 가능합니다.",
                            MIN_TODO_CONTENT_LENGTH,
                            MAX_TODO_CONTENT_LENGTH
                    ), content
            );
        }
    }

    public void completeRound(Member member) {
        if (mustDo == null) {
            throw new NecessaryTodoNotExistException(" 머스트두가 생성되지 않았습니다.", String.valueOf(id));
        }
        findRoundOfMemberBy(member.getId()).completeRound();
    }

    public boolean isMustDoDone(Member member) {
        return findRoundOfMemberBy(member.getId()).isDone();
    }

    public boolean isEndAt(LocalDate date) {
        return dayOfWeek == date.getDayOfWeek();
    }

    public RoundOfMember findRoundOfMemberBy(Long memberId) {
        return roundOfMembers.stream()
                             .filter(roundOfMember -> roundOfMember.isMemberEquals(memberId))
                             .findAny()
                             .orElseThrow(
                                     () -> new NotStudyMemberException("해당 스터디의 멤버가 아닙니다.", memberId.toString())
                             );
    }

    public int calculateProgress() {
        int doneCount = (int) roundOfMembers.stream()
                                            .filter(RoundOfMember::isDone)
                                            .count();

        return doneCount * 100 / roundOfMembers.size();
    }

    public void proceed() {
        roundStatus = RoundStatus.IN_PROGRESS;
    }

    public void finish() {
        roundStatus = RoundStatus.FINISHED;
    }

    public boolean isSuccess(Member member) {
        return findRoundOfMemberBy(member.getId()).isDone();
    }

    public boolean isMaster(Long memberId) {
        return master.equals(memberId);
    }

    public boolean isSameWeek(Integer weekNumber) {
        return this.weekNumber == weekNumber;
    }

    public boolean isSameDayOfWeek(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.equals(dayOfWeek);
    }

    public boolean isNextDayOfWeek(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.getValue() > dayOfWeek.getValue();
    }

    public boolean isInProgress() {
        return roundStatus == RoundStatus.IN_PROGRESS;
    }

    public boolean isNotStarted() {
        return roundStatus == RoundStatus.NOT_STARTED;
    }

    public int calculateLeftDaysFrom(LocalDate date) {
        int gap = dayOfWeek.getValue() - date.getDayOfWeek().getValue();
        if (gap < 0) {
            return gap + DayOfWeek.values().length;
        }
        return gap;
    }

    public boolean isBeforeOrSame(Integer minimumWeeks) {
        return weekNumber <= minimumWeeks;
    }

    public Map<Member, Integer> calculateExperience(Integer roundScore) {
        return roundOfMembers.stream()
                      .collect(toMap(RoundOfMember::getMember, roundOfMember -> roundOfMember.isDone() ? roundScore : 0));
    }

    public void exit(Long memberId) {
        if (isMaster(memberId)) {
            throw new NotStudyMemberException("스터디장은 스터디를 나갈 수 없습니다.", memberId.toString());
        }
        roundOfMembers.remove(findRoundOfMemberBy(memberId));
    }

    public Round createNextWeekRound() {
        List<RoundOfMember> list = roundOfMembers.stream()
                                                 .map(
                                                     roundOfMember -> RoundOfMember.builder()
                                                                                   .member(
                                                                                       roundOfMember.getMember())
                                                                                   .isDone(false)
                                                                                   .build())
                                                 .toList();
        return Round.builder()
                    .studyId(studyId)
                    .dayOfWeek(dayOfWeek)
                    .weekNumber(weekNumber + 1)
                    .master(master)
                    .roundOfMembers(list)
                    .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Round round = (Round) o;
        return id.equals(round.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
