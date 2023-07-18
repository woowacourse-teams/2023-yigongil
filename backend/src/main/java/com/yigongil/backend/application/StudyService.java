package com.yigongil.backend.application;

import com.yigongil.backend.domain.applicant.Applicant;
import com.yigongil.backend.domain.applicant.ApplicantRepository;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.request.StudyCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final ApplicantRepository applicantRepository;

    public StudyService(StudyRepository studyRepository, ApplicantRepository applicantRepository) {
        this.studyRepository = studyRepository;
        this.applicantRepository = applicantRepository;
    }

    @Transactional
    public Long create(Member member, StudyCreateRequest request) {
        Study study = Study.initializeStudyOf(
                request.name(),
                request.introduction(),
                request.numberOfMaximumMembers(),
                request.startAt(),
                request.totalRoundCount(),
                request.periodOfRound(),
                member
        );

        return studyRepository.save(study).getId();
    }

    @Transactional
    public void apply(Member member, Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("스터디가 존재하지 않습니다."));
        // TODO: 2023/07/18 pull 받은 뒤 StudyNotFoundException 던지는 것으로 수정

        validateApplicantAlreadyExist(member, study);
        Applicant applicant = Applicant.builder()
                .study(study)
                .member(member)
                .build();
        applicantRepository.save(applicant);
    }

    private void validateApplicantAlreadyExist(Member member, Study study) {
        boolean isApplicantAlreadyExists = applicantRepository.existsByMemberAndStudy(member, study);
        if (isApplicantAlreadyExists) {
            throw new ApplicantAlreadyExistException("이미 스터디에 신청한 멤버입니다.", String.valueOf(member.getId()));
        }
    }
}
