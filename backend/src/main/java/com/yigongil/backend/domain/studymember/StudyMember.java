package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    public void participate() {
        this.role = Role.STUDY_MEMBER;
    }

    public boolean equalsMember(Member member) {
        return this.member.equals(member);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Study getStudy() {
        return study;
    }

    public Role getRole() {
        return role;
    }

    public StudyResult getStudyResult() {
        return studyResult;
    }
}
