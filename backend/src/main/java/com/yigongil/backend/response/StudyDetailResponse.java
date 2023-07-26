package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.studymember.Role;
import com.yigongil.backend.utils.DateConverter;

import java.util.List;

public record StudyDetailResponse(
        Long id,
        Integer processingStatus,
        String name,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers,
        Long studyMasterId,
        Integer role,
        String startAt,
        Integer totalRoundCount,
        String periodOfRound,
        Integer currentRound,
        String introduction,
        List<StudyMemberResponse> members,
        List<RoundNumberResponse> rounds
) {

    public static StudyDetailResponse of(
            Study study,
            List<Round> rounds,
            Role role,
            Round currentRound,
            List<Member> members
    ) {
        return new StudyDetailResponse(
                study.getId(),
                study.getProcessingStatus().getCode(),
                study.getName(),
                currentRound.getRoundOfMembers().size(),
                study.getNumberOfMaximumMembers(),
                currentRound.getMaster().getId(),
                role.getCode(),
                DateConverter.toStringFormat(study.getStartAt()),
                study.getTotalRoundCount(),
                study.findPeriodOfRoundToString(),
                study.getCurrentRound().getRoundNumber(),
                study.getIntroduction(),
                StudyMemberResponse.from(members),
                RoundNumberResponse.from(rounds)
        );
    }
}
