package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyV1;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public record StudyDetailResponse(
        @Schema(example = "4")
        Long id,
        @Schema(example = "1")
        Integer processingStatus,
        @Schema(example = "안드로이드 뿌수기")
        String name,
        @Schema(example = "3")
        Integer numberOfCurrentMembers,
        @Schema(example = "6")
        Integer numberOfMaximumMembers,
        @Schema(example = "1")
        Long studyMasterId,
        @Schema(example = "2023.08.12")
        LocalDate startAt,
        @Schema(example = "5")
        Integer totalRoundCount,
        @Schema(example = "1w")
        String periodOfRound,
        @Schema(example = "1")
        Integer currentRound,
        @Schema(example = "코틀린으로 다 뿌수는 스터디입니다")
        String introduction,
        List<StudyMemberResponse> members,
        List<RoundNumberResponse> rounds
) {

    public static StudyDetailResponse of(
            Study study,
            List<Round> rounds,
            Round currentRound,
            List<StudyMemberResponse> studyMemberResponses
    ) {
        StudyV1 studyV1 = (StudyV1) study;
        return new StudyDetailResponse(
                studyV1.getId(),
                studyV1.getProcessingStatus().getCode(),
                studyV1.getName(),
                currentRound.getRoundOfMembers().size(),
                studyV1.getNumberOfMaximumMembers(),
                currentRound.getMaster().getId(),
                studyV1.getStartAt().toLocalDate(),
                studyV1.getTotalRoundCount(),
                studyV1.findPeriodOfRoundToString(),
                studyV1.getCurrentRound().getRoundNumber(),
                studyV1.getIntroduction(),
                studyMemberResponses,
                RoundNumberResponse.from(rounds)
        );
    }
}
