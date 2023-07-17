package com.yigongil.backend.response;

public record MyStudyResponse(
        Long id,
        Integer processingStatus,
        Boolean isMaster,
        String name,
        Integer averageTier,
        String startAt,
        String period,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers
) {
}
