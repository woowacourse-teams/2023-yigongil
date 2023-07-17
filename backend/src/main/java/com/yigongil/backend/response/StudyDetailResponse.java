package com.yigongil.backend.response;

import java.util.List;

public record StudyDetailResponse(
        Long id,
        Integer processingStatus,
        String name,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers,
        Long studyMasterId,
        String startAt,
        String period,
        Integer currentRound,
        String introduction,
        List<RecruitingStudyMemberResponse> members,
        List<RoundNumberResponse> rounds
) {
}
