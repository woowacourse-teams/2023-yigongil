package com.yigongil.backend.response;

public record MyStudyResponse(
        Long id,
        Integer processingStatus,
        Integer role,
        String name,
        Integer averageTier,
        String startAt,
        Integer totalRoundCount,
        String periodOfRound,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers
) {

}
