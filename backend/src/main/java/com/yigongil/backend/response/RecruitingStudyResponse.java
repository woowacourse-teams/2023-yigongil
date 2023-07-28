package com.yigongil.backend.response;

import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.utils.DateConverter;

public record RecruitingStudyResponse(
        Long id,
        Integer processingStatus,
        String name,
        Integer averageTier,
        String startAt,
        Integer totalRoundCount,
        String periodOfRound,
        Integer numberOfCurrentMembers,
        Integer numberOfMaximumMembers
) {

    public static RecruitingStudyResponse from(Study study) {
        return new RecruitingStudyResponse(
                study.getId(),
                study.getProcessingStatus().getCode(),
                study.getName(),
                study.calculateAverageTier(),
                DateConverter.toStringFormat(study.getStartAt()),
                study.getTotalRoundCount(),
                study.getPeriodUnit().toStringFormat(study.getPeriodOfRound()),
                study.sizeOfCurrentMembers(),
                study.getNumberOfMaximumMembers()
        );
    }
}
