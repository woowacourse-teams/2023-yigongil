package com.yigongil.backend.response;

import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyV1;
import com.yigongil.backend.domain.studymember.StudyMember;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record MyStudyResponse(
        @Schema(example = "3")
        Long id,
        @Schema(example = "1")
        Integer processingStatus,
        @Schema(example = "1")
        Integer role,
        @Schema(example = "코틀린으로 스프링하기 배울까요")
        String name,
        @Schema(example = "1")
        Integer averageTier,
        @Schema(example = "2023.08.30")
        LocalDate startAt,
        @Schema(example = "5")
        Integer totalRoundCount,
        @Schema(example = "1d")
        String periodOfRound,
        @Schema(example = "3")
        Integer numberOfCurrentMembers,
        @Schema(example = "5")
        Integer numberOfMaximumMembers
) {

    public static MyStudyResponse from(Study study, StudyMember studyMember) {
        StudyV1 studyV1 = (StudyV1) study;
        return new MyStudyResponse(
                studyV1.getId(),
                studyV1.getProcessingStatus()
                       .getCode(),
                studyMember.getRole()
                           .getCode(),
                studyV1.getName(),
                studyV1.calculateAverageTier(),
                studyV1.getStartAt().toLocalDate(),
                studyV1.getTotalRoundCount(),
                studyV1.getPeriodUnit()
                       .toStringFormat(studyV1.getPeriodOfRound()),
                studyV1.getCurrentRound()
                       .getRoundOfMembers()
                       .size(),
                studyV1.getNumberOfMaximumMembers()
        );
    }

}
