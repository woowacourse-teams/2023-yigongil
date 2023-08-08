package com.yigongil.backend.response;

import java.time.LocalDate;

public record FinishedStudyResponse(
        Long id,
        String name,
        Integer averageTier,
        LocalDate startAt,
        Integer totalRoundCount,
        String periodOfRound,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers,
        Boolean isSucceed
) {

}
