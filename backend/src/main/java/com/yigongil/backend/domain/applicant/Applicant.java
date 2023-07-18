package com.yigongil.backend.domain.applicant;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.exception.StudyMemberAlreadyExistException;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;

@Entity
public class Applicant extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Study study;

    protected Applicant() {
    }

    @Builder
    public Applicant(Long id, Member member, Study study) {
        validateStudyMemberAlreadyExist(member, study);
        this.id = id;
        this.member = member;
        this.study = study;
    }

    private void validateStudyMemberAlreadyExist(Member member, Study study) {
        Round currentRound = study.getCurrentRound();
        boolean isAlreadyMember = currentRound.getRoundOfMembers().stream()
                .anyMatch(roundOfMember -> roundOfMember.getMember().equals(member));

        if (isAlreadyMember) {
            throw new StudyMemberAlreadyExistException("이미 스터디의 구성원입니다.", String.valueOf(member.getId()));
        }
    }
}
