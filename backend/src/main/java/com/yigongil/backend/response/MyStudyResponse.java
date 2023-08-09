package com.yigongil.backend.response;

import java.time.LocalDate;

public record MyStudyResponse(
        Long id,
        Integer processingStatus,
        Integer role,
        String name,
        Integer averageTier,
        LocalDate startAt,
        Integer totalRoundCount,
        String periodOfRound,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers
) {

}
