package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.base.BaseEntity;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.Study;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class RoundOfMember extends BaseEntity {

    private static final int MAXIMUM_TODO_SIZE = 4;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(nullable = false)
    private boolean isDone;

    protected RoundOfMember() {
    }

    @Builder
    public RoundOfMember(Long id, Member member, boolean isDone) {
        this.id = id;
        this.member = member;
        this.isDone = isDone;
    }

    public static List<RoundOfMember> from(Study study) {
        return study.getStudyMembers()
                    .stream()
                    .map(studyMember -> RoundOfMember.builder()
                                                     .member(studyMember.getMember())
                                                     .isDone(false)
                                                     .build())
                    .toList();
    }

    public void completeRound() {
        this.isDone = true;
    }

    public boolean isMemberEquals(Long memberId) {
        return this.member.getId().equals(memberId);
    }
}