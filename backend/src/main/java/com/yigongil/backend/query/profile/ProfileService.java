package com.yigongil.backend.query.profile;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.member.domain.MemberRepository;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyService;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyMemberRepository;
import com.yigongil.backend.response.FinishedStudyResponse;
import com.yigongil.backend.response.ProfileResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyService studyService;
    private final MemberRepository memberRepository;

    public ProfileService(StudyMemberRepository studyMemberRepository, StudyService studyService,
        MemberRepository memberRepository) {
        this.studyMemberRepository = studyMemberRepository;
        this.studyService = studyService;
        this.memberRepository = memberRepository;
    }

    public ProfileResponse findById(Long id) {
        Member member = memberRepository.getById(id);

        List<FinishedStudyResponse> finishedStudyResponses = studyMemberRepository.findAllByMemberId(id)
                                                                                  .stream()
                                                                                  .filter(StudyMember::isStudyEnd)
                                                                                  .map(this::createFinishedStudyResponse)
                                                                                  .toList();

        return new ProfileResponse(
            member.getId(),
            member.getNickname(),
            member.getGithubId(),
            member.getProfileImageUrl(),
            studyService.calculateSuccessRate(member),
            calculateNumberOfSuccessRounds(member),
            member.calculateProgress(),
            member.getTier().getOrder(),
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
            study.sizeOfCurrentMembers(),
            study.getNumberOfMaximumMembers(),
            studyMember.isSuccess()
        );
    }

    private int calculateNumberOfSuccessRounds(Member member) {
        List<Study> studies = studyMemberRepository.findAllByMemberId(member.getId()).stream()
                                                   .filter(StudyMember::isNotApplicant)
                                                   .map(StudyMember::getStudy)
                                                   .toList();

        return (int) studies.stream()
                            .map(Study::getRounds)
                            .flatMap(List::stream)
                            .filter(round -> round.isMustDoDone(member))
                            .count();
    }
}
