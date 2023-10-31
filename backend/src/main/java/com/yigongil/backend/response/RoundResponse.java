package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;

public record RoundResponse(
        @Schema(example = "4")
        Long id,
        @Schema(example = "이번주 머스트 두")
        String mustDo,
        @Schema(example = "MONDAY")
        DayOfWeek dayOfWeek,
        @Schema(example = "IN_PROGRESS")
        RoundStatus status
) {
        public static RoundResponse from(Round round) {
                return new RoundResponse(
                        round.getId(),
                        round.getMustDo(),
                        round.getDayOfWeek(),
                        round.getRoundStatus()
                );
        }
}
