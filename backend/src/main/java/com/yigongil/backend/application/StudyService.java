package com.yigongil.backend.application;

import com.yigongil.backend.domain.applicant.Applicant;
import com.yigongil.backend.domain.applicant.ApplicantRepository;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Role;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyMemberRepository;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.exception.ApplicantNotFoundException;
import com.yigongil.backend.exception.StudyNotFoundException;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import com.yigongil.backend.utils.DateConverter;
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
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;

    public StudyService(
            StudyRepository studyRepository,
            ApplicantRepository applicantRepository,
            MemberRepository memberRepository,
            StudyMemberRepository studyMemberRepository) {
        this.studyRepository = studyRepository;
        this.applicantRepository = applicantRepository;
        this.memberRepository = memberRepository;
        this.studyMemberRepository = studyMemberRepository;
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

        studyRepository.save(study);

        StudyMember studyMember = StudyMember.builder()
                                             .study(study)
                                             .member(member)
                                             .build();

        studyMemberRepository.save(studyMember);

        return study.getId();
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
        Study study = findStudyById(studyId);

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

    @Transactional(readOnly = true)
    public StudyDetailResponse findStudyDetailByStudyId(Member member, Long studyId) {
        Study study = studyRepository.findByIdWithRound(studyId)
                .orElseThrow(() -> new StudyNotFoundException("해당 스터디가 존재하지 않습니다", studyId));

        List<Round> rounds = study.getRounds();
        Round currentRound = study.getCurrentRound();

        List<Member> members = memberRepository.findMembersByRoundId(currentRound.getId());

        return StudyDetailResponse.of(study, rounds, calculateRole(study, member), currentRound, members);
    }

    @Transactional
    public void permitApplicant(Member master, Long studyId, Long memberId) {
        Applicant applicant = findApplicantByMemberIdAndStudyId(memberId, studyId);
        Study study = findStudyById(studyId);
        study.validateMaster(master);

        applicant.participate(study);

        studyMemberRepository.save(StudyMember.from(applicant));
        applicantRepository.delete(applicant);
    }

    @Transactional(readOnly = true)
    public List<StudyMemberResponse> findApplicantsOfStudy(Long studyId, Member master) {
        Study study = findStudyById(studyId);
        study.validateMaster(master);

        List<Applicant> applicants = applicantRepository.findAllByStudy(study);

        return applicants.stream()
                         .map(Applicant::getMember)
                         .map(StudyMemberResponse::from)
                         .toList();
    }

    private Applicant findApplicantByMemberIdAndStudyId(Long memberId, Long studyId) {
        return applicantRepository.findByMemberIdAndStudyId(memberId, studyId)
                                  .orElseThrow(() -> new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId));
    }

    private Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                              .orElseThrow(() -> new StudyNotFoundException("해당 스터디를 찾을 수 없습니다", studyId));
    }

    @Transactional(readOnly = true)
    public List<MyStudyResponse> findMyStudies(Member member) {
        List<Study> studies = studyRepository.findStudiesOfMember(member);
        return studies.stream()
                      .map(study -> new MyStudyResponse(
                              study.getId(),
                              study.getProcessingStatus()
                                   .getCode(),
                              calculateRole(study, member).getCode(),
                              study.getName(),
                              study.calculateAverageTier(),
                              DateConverter.toStringFormat(study.getStartAt()),
                              study.getTotalRoundCount(),
                              study.getPeriodUnit()
                                   .toStringFormat(study.getPeriodOfRound()),
                              study.getCurrentRound()
                                   .getRoundOfMembers()
                                   .size(),
                              study.getNumberOfMaximumMembers()
                      ))
                      .toList();
    }

    private Role calculateRole(Study study, Member member) {
        if (isApplicant(study, member)) {
            return Role.APPLICANT;
        }
        return study.calculateRoleOfStartedStudy(member);
    }

    private boolean isApplicant(Study study, Member member) {
        return applicantRepository.findAllByStudy(study)
                                  .stream()
                                  .anyMatch(applicant -> applicant.getId().equals(member.getId()));
    }

    public void deleteApplicant(Member member, Long studyId) {
        Applicant applicant = findApplicantByMemberIdAndStudyId(member.getId(), studyId);
        applicantRepository.delete(applicant);
    }
}
