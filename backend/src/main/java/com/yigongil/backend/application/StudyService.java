package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.StudyNotFoundException;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.StudyDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    public StudyService(final StudyRepository studyRepository, final MemberRepository memberRepository) {
        this.studyRepository = studyRepository;
        this.memberRepository = memberRepository;
    }

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

    @Transactional(readOnly = true)
    public StudyDetailResponse findStudyDetailByStudyId(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);
        List<Round> rounds = study.getRounds();
        Round currentRound = study.getCurrentRound();

        List<Member> members = memberRepository.findMembersByRoundId(currentRound.getId());

        return StudyDetailResponse.of(study, rounds, currentRound, members);
    }
}
