package com.yigongil.backend.domain.applicant;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.Member.Member;
import com.yigongil.backend.domain.study.Study;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Applicant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Study study;

    protected Applicant() {
    }
}
