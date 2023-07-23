package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.applicant.Applicant;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Role;
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

    protected StudyMember() {
    }

    @Builder
    public StudyMember(Long id, Member member, Study study, Role role) {
        this.id = id;
        this.member = member;
        this.study = study;
        this.role = role;
    }

    public static StudyMember from(Applicant applicant) {
        return StudyMember.builder()
                .member(applicant.getMember())
                .study(applicant.getStudy())
                .build();
    }

    public boolean isNotApplicant() {
        return role != Role.APPLICANT;
    }

    public void participate() {
        this.role = Role.STUDY_MEMBER;
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
}
