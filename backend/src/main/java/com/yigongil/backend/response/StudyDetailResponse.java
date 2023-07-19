package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.utils.DateConverter;

import java.util.List;

public record StudyDetailResponse(
        Long id,
        Integer processingStatus,
        String name,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers,
        Long studyMasterId,
        String startAt,
        Integer totalRoundCount,
        Integer periodOfRound,
        Integer currentRound,
        String introduction,
        List<RecruitingStudyMemberResponse> members,
        List<RoundNumberResponse> rounds
) {

    public static StudyDetailResponse of(Study study, List<Round> rounds, Round currentRound, List<Member> members) {
        return new StudyDetailResponse(
                study.getId(),
                study.getProcessingStatus().getCode(),
                study.getName(),
                currentRound.getRoundOfMembers().size(),
                study.getNumberOfMaximumMembers(),
                currentRound.getMaster().getId(),
                DateConverter.toStringFormat(study.getStartAt()),
                study.getTotalRoundCount(),
                study.getPeriodOfRound(),
                study.getCurrentRound().getRoundNumber(),
                study.getIntroduction(),
                RecruitingStudyMemberResponse.from(members),
                RoundNumberResponse.from(rounds)
        );
    }
}
