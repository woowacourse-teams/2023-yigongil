package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
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
            List<StudyMemberResponse> studyMemberResponses
    ) {
        return new StudyDetailResponse(
                study.getId(),
                study.getProcessingStatus().getCode(),
                study.getName(),
                study.sizeOfCurrentMembers(),
                study.getNumberOfMaximumMembers(),
                study.getMaster().getId(),
                study.getStartAt().toLocalDate(),
                study.getCurrentRound().getId().intValue(), // TODO: 2021/08/12 currentRound 무슨 정보 줄 지 결정
                study.getIntroduction(),
                studyMemberResponses,
                RoundNumberResponse.from(rounds) // TODO: 2021/08/12 이건뭐지
        );
    }
}
