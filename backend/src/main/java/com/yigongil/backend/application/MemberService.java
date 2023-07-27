package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.domain.studymember.StudyMemberRepository;
import com.yigongil.backend.exception.MemberNotFoundException;
import com.yigongil.backend.request.MemberJoinRequest;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.MemberResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyService studyService;

    public MemberService(MemberRepository memberRepository, StudyMemberRepository studyMemberRepository,
                         StudyService studyService) {
        this.memberRepository = memberRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.studyService = studyService;
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다.", String.valueOf(id)));

        return new MemberResponse(
                member.getId(),
                member.getNickname(),
                member.getGithubId(),
                member.getProfileImageUrl(),
                (double) studyService.calculateSuccessRate(member),
                calculateNumberOfSuccessRounds(member),
                99, // TODO: 2023/07/27 티어 진행률은 추후 티어 진행 알고리즘 회의 후 추가
                member.getTier(),
                member.getIntroduction(),
                Collections.emptyList()
        );
    }

    private int calculateNumberOfSuccessRounds(Member member) {
        List<Study> studies = studyMemberRepository.findAllByMemberId(member.getId()).stream()
                .map(StudyMember::getStudy)
                .toList();

        int numberOfSuccessRounds = 0;
        for (Study study : studies) {
            numberOfSuccessRounds += study.getRounds().stream()
                    .filter(round -> round.isNecessaryToDoDone(member))
                    .count();
        }
        return numberOfSuccessRounds;
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
