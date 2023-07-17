package com.yigongil.backend.response;

public record RecruitingStudyResponse(
        Long id,
        Integer processingStatus,
        String name,
        Integer averageTier,
        String startAt,
        String period,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers
) {
}
