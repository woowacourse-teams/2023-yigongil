package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import java.util.ArrayList;
import java.util.List;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class Round extends BaseEntity {

    private static final int MAX_CONTENT_LENGTH = 20;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Integer roundNumber;

    @Column(length = MAX_CONTENT_LENGTH)
    private String necessaryToDoContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private Member master;

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
            Member master
    ) {
        this.id = id;
        this.roundNumber = roundNumber;
        this.necessaryToDoContent = necessaryToDoContent;
        this.master = master;

    }

    public static List<Round> of(Integer totalRoundCount, Member master) {
        List<Round> rounds = new ArrayList<>();
        for (int i = 1; i <= totalRoundCount; i++) {
            Round round = Round.builder()
                    .roundNumber(i)
                    .master(master)
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
}
