package com.yigongil.backend.application;

import com.yigongil.backend.application.studyevent.StudyStartedEvent;
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
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import com.yigongil.backend.utils.DateConverter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yigongil.backend.domain.study.PageStrategy.CREATED_AT_DESC;

@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final ApplicationEventPublisher publisher;

    public StudyService(
            StudyRepository studyRepository,
            StudyMemberRepository studyMemberRepository, ApplicationEventPublisher publisher) {
        this.studyRepository = studyRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.publisher = publisher;
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
                .studyResult(StudyResult.NONE)
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
                        .studyResult(StudyResult.NONE)
                        .build()
        );
    }

    private void validateApplicantAlreadyExist(Member member, Study study) {
        boolean cannotApply = studyMemberRepository.existsByStudyIdAndMemberId(study.getId(), member.getId());
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

        List<StudyMember> studyMembers = studyMemberRepository.findAllByStudyIdAndParticipatingAndNotEnd(studyId);

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
        int successRate = calculateSuccessRate(member);

        return new StudyMemberResponse(
                member.getId(),
                member.getTier(),
                member.getNickname(),
                successRate,
                member.getProfileImageUrl()
        );
    }

    public int calculateSuccessRate(Member member) {
        Long success = studyMemberRepository.countByMemberIdAndStudyResult(member.getId(), StudyResult.SUCCESS);
        Long fail = studyMemberRepository.countByMemberIdAndStudyResult(member.getId(), StudyResult.FAIL);
        int successRate = 0;
        if (Objects.nonNull(success) && Objects.nonNull(fail) && success + fail != 0) {
            successRate = (int) (success * 100 / success + fail);
        }
        return successRate;
    }
  
    @Transactional(readOnly = true)
    public List<MyStudyResponse> findMyStudies(Member member) {
        List<StudyMember> studyMembers = studyMemberRepository.findAllByMemberIdAndParticipatingAndNotEnd(member.getId());

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
                            DateConverter.toStringFormat(study.getStartAt()),
                            study.getTotalRoundCount(),
                            study.getPeriodUnit()
                                    .toStringFormat(study.getPeriodOfRound()),
                            study.getCurrentRound()
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
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId));
        if (studyMember.isNotApplicant()) {
            throw new ApplicantNotFoundException("해당 지원자가 존재하지 않습니다.", memberId);
        }
        return studyMember;
    }

    @Transactional
    public void startStudy(Member member, Long studyId) {
        Study study = findStudyById(studyId);
        study.validateMaster(member);

        study.startStudy();
        publisher.publishEvent(new StudyStartedEvent(study));
    }

    private Study findStudyById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new StudyNotFoundException("해당 스터디를 찾을 수 없습니다", studyId));
    }
}
