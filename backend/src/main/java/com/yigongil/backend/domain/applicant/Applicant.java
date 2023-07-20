package com.yigongil.backend.domain.applicant;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.exception.InvalidProcessingStatusException;
import com.yigongil.backend.exception.NotStudyApplicantException;
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
        validateStudyProcessingStatus(study);
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

    private void validateStudyProcessingStatus(Study study) throws InvalidProcessingStatusException {
        if (!study.isRecruiting()) {
            String processingStatus = study.getProcessingStatus().name();
            throw new InvalidProcessingStatusException("지원한 스터디는 현재 모집 중인 상태가 아닙니다.", processingStatus);
        }
    }

    public void participate(Study study) {
        checkStudy(study);
        study.addMember(this.member);
    }

    private void checkStudy(Study study) {
        if (!this.study.equals(study)) {
            throw new NotStudyApplicantException("해당 스터디에 지원한 신청자가 아닙니다.", id);
        }
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
