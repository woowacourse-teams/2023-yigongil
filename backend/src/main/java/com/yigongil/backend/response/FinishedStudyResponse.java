package com.yigongil.backend.response;

public record FinishedStudyResponse(
        Long id,
        String name,
        Integer averageTier,
        String startAt,
        String period,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers,
        Boolean isSucceed
) {
}
