package com.yigongil.backend.query.profile;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.member.domain.MemberRepository;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyService;
import com.yigongil.backend.domain.study.studymember.StudyMember;
import com.yigongil.backend.domain.study.studymember.StudyMemberRepository;
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
    private final RoundRepository roundRepository;


    public ProfileService(StudyMemberRepository studyMemberRepository, StudyService studyService,
        MemberRepository memberRepository, RoundRepository roundRepository) {
        this.studyMemberRepository = studyMemberRepository;
        this.studyService = studyService;
        this.memberRepository = memberRepository;
        this.roundRepository = roundRepository;
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
                            .map(study -> roundRepository.findAllByStudyId(study.getId()))
                            .flatMap(List::stream)
                            .filter(round -> round.isMustDoDone(member))
                            .count();
    }
}
