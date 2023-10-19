package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import java.util.Objects;
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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
public class StudyMember extends BaseEntity {

    private static final int EXPERIENCE_BASE_UNIT = 1;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public boolean isStudyMember() {
        return this.role == Role.STUDY_MEMBER || isMaster();
    }

    public boolean isApplicant() {
        return this.role == Role.APPLICANT;
    }

    public boolean isMaster() {
        return this.role == Role.MASTER;
    }

    public void completeSuccessfully() {
        int successfulRoundCount = study.calculateSuccessfulRoundCount(member);
        int defaultRoundExperience = EXPERIENCE_BASE_UNIT * 2;
        int additionalExperienceOfPeriodLength = EXPERIENCE_BASE_UNIT * 3 / study.getMeetingDaysCountPerWeek() + 1;
        int totalExperience = successfulRoundCount * (defaultRoundExperience + additionalExperienceOfPeriodLength);
        member.addExperience(totalExperience);
        this.studyResult = StudyResult.SUCCESS;
    }

    public void failStudy() {
        this.studyResult = StudyResult.FAIL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudyMember studyMember)) {
            return false;
        }
        if (id == null || studyMember.getId() == null) {
            return false;
        }
        return id.equals(studyMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
