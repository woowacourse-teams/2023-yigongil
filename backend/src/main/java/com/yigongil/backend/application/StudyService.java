package com.yigongil.backend.application;

import static com.yigongil.backend.domain.study.PageStrategy.ID_DESC;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.domain.studymember.Role;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyMemberRepository;
import com.yigongil.backend.domain.studymember.StudyResult;
import com.yigongil.backend.exception.ApplicantAlreadyExistException;
import com.yigongil.backend.exception.ApplicantNotFoundException;
import com.yigongil.backend.exception.StudyNotFoundException;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    public StudyService(
            StudyRepository studyRepository,
            StudyMemberRepository studyMemberRepository
    ) {
        this.studyRepository = studyRepository;
        this.studyMemberRepository = studyMemberRepository;
    }

    @Transactional
    public Long create(Member member, StudyUpdateRequest request) {
        Study study = Study.initializeStudyOf(
                request.name(),
                request.introduction(),
                request.numberOfMaximumMembers(),
                request.startAt().atStartOfDay(),
                request.totalRoundCount(),
                request.periodOfRound(),
                member
        );

        studyRepository.save(study);

        StudyMember studyMember = StudyMember.builder()
                                             .study(study)
                                             .member(member)
                                             .role(Role.MASTER)
                                             .studyResult(StudyResult.NONE)
                                             .build();

        studyMemberRepository.save(studyMember);

        return study.getId();
    }

    @Transactional(readOnly = true)
    public List<RecruitingStudyResponse> findRecruitingStudies(int page) {
        Pageable pageable = PageRequest.of(page, ID_DESC.getSize(), ID_DESC.getSort());

        Page<Study> studies = studyRepository.findAllByProcessingStatus(ProcessingStatus.RECRUITING, pageable);
        return toRecruitingStudyResponse(studies);

    }

    @Transactional(readOnly = true)
    public List<RecruitingStudyResponse> findRecruitingStudiesWithSearch(int page, String word) {
        Pageable pageable = PageRequest.of(page, ID_DESC.getSize(), ID_DESC.getSort());
        Page<Study> studies = studyRepository.findAllByProcessingStatusAndNameContainingIgnoreCase(
                ProcessingStatus.RECRUITING,
                word,
                pageable
        );
        return toRecruitingStudyResponse(studies);
    }

    private List<RecruitingStudyResponse> toRecruitingStudyResponse(Page<Study> studies) {
        return studies.get()
                      .map(RecruitingStudyResponse::from)
                      .toList();
    }

    @Transactional
    public void apply(Member member, Long studyId) {
        Study study = findStudyById(studyId);

        study.validateMemberSize();
        validateApplicantAlreadyExist(member, study);

        studyMemberRepository.save(
                StudyMember.builder()
                           .study(study)
                           .member(member)
                           .role(Role.APPLICANT)
                           .studyResult(StudyResult.NONE)
                           .build()
        );
    }

    public Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                              .orElseThrow(() -> new StudyNotFoundException("해당 스터디를 찾을 수 없습니다", studyId));
    }

    private void validateApplicantAlreadyExist(Member member, Study study) {
        boolean cannotApply = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), member.getId());
        if (cannotApply) {
            throw new ApplicantAlreadyExistException("스터디에 신청할 수 없는 멤버입니다.", String.valueOf(member.getId()));
        }
    }

    @Transactional(readOnly = true)
    public StudyDetailResponse findStudyDetailByStudyId(Member member, Long studyId) {
        Study study = findStudyById(studyId);

        List<Round> rounds = study.getRounds();
        Round currentRound = study.currentRound();

        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyIdAndRoleNotAndStudyResult(studyId, Role.APPLICANT, StudyResult.NONE);

        Role role = studyMemberRepository.findByStudyIdAndMemberId(studyId, member.getId())
                                         .map(StudyMember::getRole)
                                         .orElse(Role.NO_ROLE);

        return StudyDetailResponse.of(study, rounds, role, currentRound, createStudyMemberResponses(studyMembers));
    }

    @Transactional
    public void permitApplicant(Member master, Long studyId, Long memberId) {
        StudyMember studyMember = findApplicantByMemberIdAndStudyId(memberId, studyId);
        Study study = studyMember.getStudy();

        study.validateMaster(master);

        study.addMember(studyMember.getMember());
        studyMember.participate();
    }

    @Transactional(readOnly = true)
    public List<StudyMemberResponse> findApplicantsOfStudy(Long studyId, Member master) {
        Study study = findStudyById(studyId);
        study.validateMaster(master);
        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyIdAndRole(studyId, Role.APPLICANT);

        return createStudyMemberResponses(studyMembers);
    }

    private List<StudyMemberResponse> createStudyMemberResponses(List<StudyMember> studyMembers) {
        return studyMembers.stream()
                           .map(this::createStudyMemberResponse)
                           .toList();
    }


    private StudyMemberResponse createStudyMemberResponse(StudyMember studyMember) {
        Member member = studyMember.getMember();

        return new StudyMemberResponse(
                member.getId(),
                member.getTier(),
                member.getNickname(),
                calculateSuccessRate(member),
                member.getProfileImageUrl(),
                member.isDeleted()
        );
    }

    public double calculateSuccessRate(Member member) {
        Long success = studyMemberRepository.countByMemberIdAndStudyResult(member.getId(), StudyResult.SUCCESS);
        Long fail = studyMemberRepository.countByMemberIdAndStudyResult(member.getId(), StudyResult.FAIL);
        if (success + fail == 0) {
            return 0;
        }
        return ((double) (success * 100) / success + fail);
    }

    @Transactional(readOnly = true)
    public List<MyStudyResponse> findMyStudies(Member member) {
        List<StudyMember> studyMembers = studyMemberRepository.findAllByMemberIdAndRoleNotAndStudyResult(
                member.getId(),
                Role.APPLICANT,
                StudyResult.NONE
        );

        List<MyStudyResponse> response = new ArrayList<>();
        for (StudyMember studyMember : studyMembers) {
            Study study = studyMember.getStudy();
            response.add(
                    new MyStudyResponse(
                            study.getId(),
                            study.getProcessingStatus()
                                 .getCode(),
                            studyMember.getRole()
                                       .getCode(),
                            study.getName(),
                            study.calculateAverageTier(),
                            study.getStartAt().toLocalDate(),
                            study.getTotalRoundCount(),
                            study.getPeriodUnit()
                                 .toStringFormat(study.getPeriodOfRound()),
                            study.currentRound()
                                 .getRoundOfMembers()
                                 .size(),
                            study.getNumberOfMaximumMembers()
                    )
            );
        }
        return response;
    }

    @Transactional
    public void deleteApplicant(Member member, Long studyId) {
        StudyMember applicant = findApplicantByMemberIdAndStudyId(member.getId(), studyId);
        studyMemberRepository.delete(applicant);
    }

    private StudyMember findApplicantByMemberIdAndStudyId(Long memberId, Long studyId) {
        StudyMember studyMember = studyMemberRepository
                .findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId));
        if (studyMember.isNotApplicant()) {
            throw new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId);
        }
        return studyMember;
    }

    @Transactional
    public void proceedRound(LocalDate today) {
        List<Study> studies = studyRepository.findAllByProcessingStatus(ProcessingStatus.PROCESSING);

        studies.stream()
               .filter(study -> study.isCurrentRoundEndAt(today))
               .forEach(Study::updateToNextRound);

        studies.stream()
               .filter(Study::isEnd)
               .map(endedStudy -> studyMemberRepository.findAllByStudyId(endedStudy.getId()))
               .flatMap(Collection::stream)
               .forEach(StudyMember::completeSuccessfully);
    }

    @Transactional
    public void startStudy(Member member, Long studyId) {
        studyMemberRepository.deleteAllByStudyIdAndRole(studyId, Role.APPLICANT);
        Study study = findStudyById(studyId);
        study.validateMaster(member);

        study.startStudy();
    }

    @Transactional
    public void update(Member member, Long studyId, StudyUpdateRequest request) {
        Study study = findStudyById(studyId);
        study.updateInformation(
                member,
                request.name(),
                request.numberOfMaximumMembers(),
                request.startAt().atStartOfDay(),
                request.totalRoundCount(),
                request.periodOfRound(),
                request.introduction()
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteStudyByMasterId(Long memberId) {
        List<Study> studies = studyRepository.findAllByMasterIdAndProcessingStatus(memberId, ProcessingStatus.RECRUITING);
        studyRepository.deleteAllByStudies(studies);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeDeletedMemberFromRecruitingStudy(Long memberId) {
        List<Study> studies = studyRepository.findByMemberIdAndProcessingStatus(memberId, ProcessingStatus.RECRUITING);
        for (Study study : studies) {
            study.removeAllRoundMemberByMemberId(memberId);
        }
        studyMemberRepository.deleteByStudyInAndMemberId(studies, memberId);
    }
}
