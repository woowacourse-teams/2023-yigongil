package com.yigongil.backend.response;

public record FinishedStudyResponse(
        Long id,
        String name,
        Integer averageTier,
        String startAt,
        Integer totalRoundCount,
        String periodOfRound,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers,
        Boolean isSucceed
) {
}
