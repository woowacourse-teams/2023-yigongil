package com.yigongil.backend.application;

import com.yigongil.backend.domain.applicant.Applicant;
import com.yigongil.backend.domain.applicant.ApplicantRepository;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.RecruitingStudyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yigongil.backend.domain.study.PageStrategy.CREATED_AT_DESC;

@Transactional
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

        return studyRepository.save(study)
                              .getId();
    }

    @Transactional(readOnly = true)
    public List<RecruitingStudyResponse> findRecruitingStudies(int page) {
        Pageable pageable = PageRequest.of(page, CREATED_AT_DESC.getSize(), CREATED_AT_DESC.getSort());
        Page<Study> studies = studyRepository.findAllByProcessingStatus(ProcessingStatus.RECRUITING, pageable);

        return studies.get()
                      .map(RecruitingStudyResponse::from)
                      .toList();
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
