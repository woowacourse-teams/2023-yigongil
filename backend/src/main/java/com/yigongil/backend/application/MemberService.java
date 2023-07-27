package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyMemberRepository;
import com.yigongil.backend.exception.MemberNotFoundException;
import com.yigongil.backend.request.MemberJoinRequest;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.FinishedStudyResponse;
import com.yigongil.backend.response.ProfileResponse;
import com.yigongil.backend.utils.DateConverter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyService studyService;

    public MemberService(
            MemberRepository memberRepository,
            StudyMemberRepository studyMemberRepository,
            StudyService studyService
    ) {
        this.memberRepository = memberRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.studyService = studyService;
    }

    @Transactional(readOnly = true)
    public ProfileResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다.", String.valueOf(id)));

        List<FinishedStudyResponse> finishedStudyResponses = studyMemberRepository.findAllByMemberId(member.getId())
                .stream()
                .filter(StudyMember::isStudyEnd)
                .map(this::createFinishedStudyResponse)
                .toList();

        return new ProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getGithubId(),
                member.getProfileImageUrl(),
                (double) studyService.calculateSuccessRate(member),
                calculateNumberOfSuccessRounds(member),
                99, // TODO: 2023/07/27 티어 진행률은 추후 티어 진행 알고리즘 회의 후 추가
                member.getTier(),
                member.getIntroduction(),
                finishedStudyResponses
        );
    }

    private FinishedStudyResponse createFinishedStudyResponse(StudyMember studyMember) {
        Study study = studyMember.getStudy();
        return new FinishedStudyResponse(
                study.getId(),
                study.getName(),
                study.calculateAverageTier(),
                DateConverter.toStringFormat(study.getStartAt()),
                study.getTotalRoundCount(),
                study.getPeriodUnit().toStringFormat(study.getPeriodOfRound()),
                study.sizeOfCurrentMembers(),
                study.getNumberOfMaximumMembers(),
                studyMember.isSuccess()
        );
    }

    private int calculateNumberOfSuccessRounds(Member member) {
        List<Study> studies = studyMemberRepository.findAllByMemberId(member.getId()).stream()
                .map(StudyMember::getStudy)
                .toList();

        return (int) studies.stream()
                .map(Study::getRounds)
                .flatMap(List::stream)
                .filter(round -> round.isNecessaryToDoDone(member))
                .count();
    }

    @Transactional
    public void update(Member member, ProfileUpdateRequest request) {
        member.updateProfile(request.nickname(), request.introduction());
    }

    @Transactional
    public Long join(MemberJoinRequest request) {
        Member member = memberRepository.save(
                Member.builder()
                        .githubId(request.githubId())
                        .tier(1)
                        .build()
        );

        return member.getId();
    }
}
