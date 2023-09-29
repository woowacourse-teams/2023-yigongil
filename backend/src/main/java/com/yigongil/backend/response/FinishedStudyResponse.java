package com.yigongil.backend.response;

import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyV1;
import com.yigongil.backend.domain.studymember.StudyMember;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record FinishedStudyResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "자바스크립트 공부해")
        String name,
        @Schema(example = "4")
        Integer averageTier,
        @Schema(example = "2023.08.12")
        LocalDate startAt,
        @Schema(example = "5")
        Integer totalRoundCount,
        @Schema(example = "1w")
        String periodOfRound,
        @Schema(example = "3")
        Integer numberOfCurrentMembers,
        @Schema(example = "4")
        Integer numberOfMaximumMembers,
        @Schema(example = "true")
        Boolean isSucceed
) {

    public static FinishedStudyResponse from(Study study, StudyMember studyMember) {
        StudyV1 studyV1 = (StudyV1) study;
        return new FinishedStudyResponse(
                studyV1.getId(),
                studyV1.getName(),
                studyV1.calculateAverageTier(),
                studyV1.getStartAt().toLocalDate(),
                studyV1.getTotalRoundCount(),
                studyV1.getPeriodUnit().toStringFormat(studyV1.getPeriodOfRound()),
                studyV1.sizeOfCurrentMembers(),
                studyV1.getNumberOfMaximumMembers(),
                studyMember.isSuccess()
        );
    }

}
