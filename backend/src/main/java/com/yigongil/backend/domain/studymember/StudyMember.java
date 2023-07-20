package com.yigongil.backend.domain.studymember;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.applicant.Applicant;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;

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

    protected StudyMember() {
    }

    @Builder
    public StudyMember(Long id, Member member, Study study) {
        this.id = id;
        this.member = member;
        this.study = study;
    }

    public static StudyMember from(Applicant applicant) {
        return StudyMember.builder()
                .member(applicant.getMember())
                .study(applicant.getStudy())
                .build();
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
}
