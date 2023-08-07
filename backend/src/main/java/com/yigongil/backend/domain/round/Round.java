package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.exception.InvalidTodoLengthException;
import com.yigongil.backend.exception.NecessaryTodoAlreadyExistException;
import com.yigongil.backend.exception.NotStudyMasterException;
import com.yigongil.backend.exception.NotStudyMemberException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Getter
@Entity
public class Round extends BaseEntity {

    private static final int MAX_TODO_CONTENT_LENGTH = 20;
    private static final int MIN_TODO_CONTENT_LENGTH = 1;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer roundNumber;

    @Column(length = MAX_TODO_CONTENT_LENGTH)
    private String necessaryToDoContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member master;

    private LocalDateTime endAt;

    @Cascade(CascadeType.PERSIST)
    @OneToMany
    @JoinColumn(name = "round_id", nullable = false)
    private List<RoundOfMember> roundOfMembers = new ArrayList<>();

    protected Round() {
    }

    @Builder
    public Round(
            Long id,
            Integer roundNumber,
            String necessaryToDoContent,
            Member master,
            LocalDateTime endAt,
            List<RoundOfMember> roundOfMembers
    ) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.necessaryToDoContent = necessaryToDoContent;
        this.master = master;
        this.endAt = endAt;
        this.roundOfMembers = roundOfMembers == null ? new ArrayList<>() : roundOfMembers;
    }

    public static List<Round> of(Integer totalRoundCount, Member master) {
        List<Round> rounds = new ArrayList<>();
        for (int i = 1; i <= totalRoundCount; i++) {
            Round round = Round.builder()
                               .roundNumber(i)
                               .master(master)
                               .roundOfMembers(new ArrayList<>())
                               .build();

            RoundOfMember roundOfMember = RoundOfMember.builder()
                                                       .member(master)
                                                       .isDone(false)
                                                       .build();
            round.roundOfMembers.add(roundOfMember);

            rounds.add(round);
        }
        return rounds;
    }

    public Long createNecessaryTodo(Member author, String content) {
        validateTodoLength(content);
        validateMaster(author);
        if (Objects.nonNull(necessaryToDoContent)) {
            throw new NecessaryTodoAlreadyExistException("필수 투두가 이미 존재합니다.", necessaryToDoContent);
        }
        necessaryToDoContent = content;
        return id;
    }

    public void validateMaster(Member member) {
        if (master.getId().equals(member.getId())) {
            return;
        }
        throw new NotStudyMasterException("스터디 마스터가 아니라 권한이 없습니다 ", member.getNickname());
    }

    public OptionalTodo createOptionalTodo(Member author, String content) {
        validateTodoLength(content);
        RoundOfMember targetRoundOfMember = findRoundOfMemberBy(author);
        return targetRoundOfMember.createOptionalTodo(content);
    }

    private void validateTodoLength(String content) {
        int contentLength = content.strip().length();
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

    public int calculateAverageTier() {
        double averageTier = roundOfMembers.stream()
                                           .map(RoundOfMember::getMember)
                                           .mapToInt(Member::getTier)
                                           .average()
                                           .orElseThrow(IllegalStateException::new);

        return (int) Math.round(averageTier);
    }

    public void addMember(Member member) {
        RoundOfMember roundOfMember = RoundOfMember.builder()
                                                   .member(member)
                                                   .isDone(false)
                                                   .build();

        roundOfMembers.add(roundOfMember);
    }

    public int sizeOfCurrentMembers() {
        return roundOfMembers.size();
    }

    public void updateNecessaryTodoIsDone(Member member, Boolean isDone) {
        findRoundOfMemberBy(member).updateNecessaryTodoIsDone(isDone);
    }

    public boolean isNecessaryToDoDone(Member member) {
        return findRoundOfMemberBy(member).isDone();
    }

    public RoundOfMember findRoundOfMemberBy(Member member) {
        return roundOfMembers.stream()
                             .filter(roundOfMember -> roundOfMember.isMemberEquals(member))
                             .findAny()
                             .orElseThrow(() -> new NotStudyMemberException("해당 스터디의 멤버가 아닙니다.",
                                     member.getGithubId()));
    }

    public void updateNecessaryTodoContent(String content) {
        necessaryToDoContent = content;
    }

    public boolean isEndAt(LocalDate today) {
        LocalDate endAtDate = endAt.toLocalDate();
        return endAtDate.equals(today);
    }

    public void updateEndAt(LocalDateTime endAt) {
        this.endAt = LocalDateTime.of(endAt.toLocalDate(), LocalTime.MIN);
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
