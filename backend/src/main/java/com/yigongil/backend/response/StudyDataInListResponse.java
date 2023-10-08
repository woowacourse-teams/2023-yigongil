package com.yigongil.backend.response;

import com.yigongil.backend.domain.study.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record StudyDataInListResponse(
        @Schema(example = "2")
        Long id,
        @Schema(example = "1")
        Integer processingStatus,
        @Schema(example = "자바 스프링 잘하는법")
        String name,
        @Schema(example = "2")
        Integer averageTier,
        @Schema(example = "2023.08.13")
        LocalDate createdAt,
        @Schema(example = "6")
        Integer numberOfCurrentMembers,
        @Schema(example = "6")
        Integer numberOfMaximumMembers,
        @Schema(example = "2")
        Integer meetingDaysPerWeek,
        @Schema(example = "8")
        Integer minimumWeeks
) {

    public static StudyDataInListResponse from(Study study) {
        return new StudyDataInListResponse(
                study.getId(),
                study.getProcessingStatus().getCode(),
                study.getName(),
                study.calculateAverageTier(),
                study.getCreatedAt().toLocalDate(),
                study.sizeOfCurrentMembers(),
                study.getNumberOfMaximumMembers(),
                study.getMeetingDaysPerWeek(),
                study.getMinimumWeeks()
        );
    }
}
