package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Role;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyMemberRepository;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.exception.ApplicantNotFoundException;
import com.yigongil.backend.exception.InvalidStudyMemberException;
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
    private final StudyMemberRepository studyMemberRepository;

    public StudyService(
            StudyRepository studyRepository,
            StudyMemberRepository studyMemberRepository) {
        this.studyRepository = studyRepository;
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
                .role(Role.MASTER)
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

        studyMemberRepository.save(
                StudyMember.builder()
                        .study(study)
                        .member(member)
                        .role(Role.APPLICANT)
                        .build()
        );
    }

    private void validateApplicantAlreadyExist(Member member, Study study) {
        boolean cannotApply = studyMemberRepository.existsByStudyAndMember(study, member);
        if (cannotApply) {
            throw new ApplicantAlreadyExistException("스터디에 신청할 수 없는 멤버입니다.", String.valueOf(member.getId()));
        }
    }

    @Transactional(readOnly = true)
    public StudyDetailResponse findStudyDetailByStudyId(Member member, Long studyId) {
        Study study = studyRepository.findByIdWithRound(studyId)
                .orElseThrow(() -> new StudyNotFoundException("해당 스터디가 존재하지 않습니다", studyId));

        List<Round> rounds = study.getRounds();
        Round currentRound = study.getCurrentRound();

        List<Member> members = studyMemberRepository.findAllByStudyIdAndRole(studyId, Role.STUDY_MEMBER).stream()
                .map(StudyMember::getMember)
                .toList();

        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, member.getId())
                .orElseThrow(() -> new InvalidStudyMemberException("해당 스터디에 멤버가 존재하지 않습니다.", String.valueOf(studyId)));

        return StudyDetailResponse.of(study, rounds, studyMember.getRole(), currentRound, members);
    }

    @Transactional
    public void permitApplicant(Member master, Long studyId, Long memberId) {
        StudyMember studyMember = findApplicantByMemberIdAndStudyId(memberId, studyId);
        Study study = findStudyById(studyId);

        study.validateMaster(master);

        studyMember.participate();
    }

    @Transactional(readOnly = true)
    public List<StudyMemberResponse> findApplicantsOfStudy(Long studyId, Member master) {
        Study study = findStudyById(studyId);
        study.validateMaster(master);
        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyIdAndRole(studyId, Role.APPLICANT);

        return studyMembers.stream()
                .map(StudyMember::getMember)
                .map(StudyMemberResponse::from)
                .toList();
    }

    private StudyMember findApplicantByMemberIdAndStudyId(Long memberId, Long studyId) {
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId));
        if (studyMember.isNotApplicant()) {
            throw new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId);
        }
        return studyMember;
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
                        studyMemberRepository.findByStudyIdAndMemberId(study.getId(), member.getId()).get()
                                .getRole()
                                .getCode(),
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

    @Transactional
    public void deleteApplicant(Member member, Long studyId) {
        StudyMember applicant = findApplicantByMemberIdAndStudyId(member.getId(), studyId);
        studyMemberRepository.delete(applicant);
    }
}
