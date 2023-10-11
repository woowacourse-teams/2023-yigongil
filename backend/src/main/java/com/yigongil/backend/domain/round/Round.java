package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.meetingdayoftheweek.MeetingDayOfTheWeek;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.exception.NecessaryTodoAlreadyExistException;
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

    @Column(length = MAX_TODO_CONTENT_LENGTH)
    private String necessaryToDoContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member master;

    @Cascade(CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany
    @JoinColumn(name = "round_id", nullable = false)
    private List<RoundOfMember> roundOfMembers = new ArrayList<>();

    @Cascade(CascadeType.PERSIST)
    @ManyToOne(fetch = FetchType.LAZY)
    private MeetingDayOfTheWeek meetingDayOfTheWeek;

    @Column(nullable = false)
    private Integer weekNumber;

    @Enumerated(EnumType.STRING)
    private RoundStatus roundStatus = RoundStatus.NOT_STARTED;

    protected Round() {
    }

    @Builder
    public Round(
            Long id,
            String necessaryToDoContent,
            Member master,
            List<RoundOfMember> roundOfMembers,
            MeetingDayOfTheWeek meetingDayOfTheWeek,
            Integer weekNumber
    ) {
        this.id = id;
        this.necessaryToDoContent = necessaryToDoContent;
        this.master = master;
        this.roundOfMembers = roundOfMembers == null ? new ArrayList<>() : roundOfMembers;
        this.meetingDayOfTheWeek = meetingDayOfTheWeek;
        this.weekNumber = weekNumber;
    }

    public static Round of(MeetingDayOfTheWeek meetingDayOfTheWeek, Study study, Integer weekNumber) {
        return Round.builder()
                    .meetingDayOfTheWeek(meetingDayOfTheWeek)
                    .weekNumber(weekNumber)
                    .master(study.getMaster())
                    .roundOfMembers(RoundOfMember.from(study))
                    .build();
    }

    public void createNecessaryTodo(Member author, String content) {
        validateTodoLength(content);
        validateMaster(author);
        if (necessaryToDoContent != null) {
            throw new NecessaryTodoAlreadyExistException("필수 투두가 이미 존재합니다.", necessaryToDoContent);
        }
        necessaryToDoContent = content;
    }

    public void validateMaster(Member member) {
        if (master.getId().equals(member.getId())) {
            return;
        }
        throw new NotStudyMasterException("필수 투두를 수정할 권한이 없습니다.", member.getNickname());
    }

    private void validateTodoLength(String content) {
        int contentLength = content.length();
        if (contentLength > MAX_TODO_CONTENT_LENGTH || contentLength < MIN_TODO_CONTENT_LENGTH) {
            throw new InvalidTodoLengthException(
                    String.format(
                            "투두 길이는 %d자 이상 %d자 이하로 작성 가능합니다.",
                            MIN_TODO_CONTENT_LENGTH,
                            MAX_TODO_CONTENT_LENGTH
                    ), content
            );
        }
    }

    public void completeRound(Member member) {
        if (necessaryToDoContent == null) {
            throw new NecessaryTodoNotExistException("필수 투두가 생성되지 않았습니다.", String.valueOf(id));
        }
        findRoundOfMemberBy(member).completeRound();
    }

    public int sizeOfCurrentMembers() {
        return roundOfMembers.size();
    }

    public void updateNecessaryTodoIsDone(Member member, Boolean isDone) {
        if (necessaryToDoContent == null) {
            throw new NecessaryTodoNotExistException("필수 투두가 생성되지 않았습니다.", String.valueOf(id));
        }
        findRoundOfMemberBy(member).updateNecessaryTodoIsDone(isDone);
    }

    public boolean isNecessaryToDoDone(Member member) {
        return findRoundOfMemberBy(member).isDone();
    }

    public void updateNecessaryTodoContent(Member member, String content) {
        if (!master.equals(member)) {
            throw new NotStudyMasterException("필수 투두를 수정할 권한이 없습니다.", String.valueOf(member.getNickname()));
        }
        necessaryToDoContent = content;
    }

    public boolean isEndAt(LocalDate date) {
        return meetingDayOfTheWeek.isSameDayOfWeek(date.getDayOfWeek());
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

    public boolean isSameDayOfWeek(MeetingDayOfTheWeek meetingDayOfTheWeek) {
        return this.meetingDayOfTheWeek.equals(meetingDayOfTheWeek);
    }

    public boolean isNextDayOfWeek(DayOfWeek dayOfWeek) {
        return this.meetingDayOfTheWeek.comesNext(dayOfWeek);
    }

    public boolean isInProgress() {
        return roundStatus == RoundStatus.IN_PROGRESS;
    }

    public int calculateLeftDaysFrom(LocalDate date) {
        int gap = meetingDayOfTheWeek.getDayOfWeek().getValue() - date.getDayOfWeek().getValue();
        if (gap < 0) {
            return gap + DayOfWeek.values().length;
        }
        return gap;
    }

    public DayOfWeek getDayOFWeek() {
        return meetingDayOfTheWeek.getDayOfWeek();
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
