package com.yigongil.backend.domain.round;

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
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class Round extends BaseEntity {

    private static final int MAX_TODO_CONTENT_LENGTH = 20;
    private static final int MIN_TODO_CONTENT_LENGTH = 1;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Column(length = MAX_TODO_CONTENT_LENGTH)
    private String mustDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member master;

    @Cascade(CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany
    @JoinColumn(name = "round_id", nullable = false)
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
            Study study,
            String mustDo,
            Member master,
            List<RoundOfMember> roundOfMembers,
            DayOfWeek dayOfWeek,
            Integer weekNumber
    ) {
        this.id = id;
        this.study = study;
        this.mustDo = mustDo;
        this.master = master;
        this.roundOfMembers = roundOfMembers == null ? new ArrayList<>() : roundOfMembers;
        this.dayOfWeek = dayOfWeek;
        this.weekNumber = weekNumber;
    }

    public static Round of(DayOfWeek dayOfWeek, Study study, Integer weekNumber) {
        return Round.builder()
                    .study(study)
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
        findRoundOfMemberBy(member).completeRound();
    }

    public boolean isMustDoDone(Member member) {
        return findRoundOfMemberBy(member).isDone();
    }

    public boolean isEndAt(LocalDate date) {
        return dayOfWeek == date.getDayOfWeek();
    }

    public RoundOfMember findRoundOfMemberBy(Member member) {
        return roundOfMembers.stream()
                             .filter(roundOfMember -> roundOfMember.isMemberEquals(member))
                             .findAny()
                             .orElseThrow(
                                     () -> new NotStudyMemberException("해당 스터디의 멤버가 아닙니다.", member.getGithubId())
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
        return findRoundOfMemberBy(member).isDone();
    }

    public boolean isMaster(Member member) {
        return master.equals(member);
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

    public void exit(Member member) {
        if (isMaster(member)) {
            throw new NotStudyMemberException("스터디장은 스터디를 나갈 수 없습니다.", member.getGithubId());
        }
        roundOfMembers.remove(findRoundOfMemberBy(member));
    }

    public Round createNextWeekRound() {
        return of(dayOfWeek, study, weekNumber + 1);
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
