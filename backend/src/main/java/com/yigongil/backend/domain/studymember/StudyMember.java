package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.study.Study;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class StudyMember extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Study study;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudyResult studyResult;

    protected StudyMember() {
    }

    @Builder
    public StudyMember(Long id, Member member, Study study, Role role, StudyResult studyResult) {
        this.id = id;
        this.member = member;
        this.study = study;
        this.role = role;
        this.studyResult = studyResult;
    }

    public boolean isNotApplicant() {
        return role != Role.APPLICANT;
    }

    public boolean isStudyEnd() {
        return this.studyResult != StudyResult.NONE;
    }

    public boolean isSuccess() {
        return this.studyResult == StudyResult.SUCCESS;
    }

    public void participate() {
        this.role = Role.STUDY_MEMBER;
    }

    public void completeSuccessfully() {
        int successfulRoundCount = (int) study.getRounds().stream()
                                              .map(round -> round.findRoundOfMemberBy(member))
                                              .filter(RoundOfMember::isDone)
                                              .count();
        member.addExperience(successfulRoundCount * (2 + 1 * study.calculateStudyPeriod()));
        this.studyResult = StudyResult.SUCCESS;
    }

    public boolean equalsMember(Member member) {
        return this.member.equals(member);
    }
}
