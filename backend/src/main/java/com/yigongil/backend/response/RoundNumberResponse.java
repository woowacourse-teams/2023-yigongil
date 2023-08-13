package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.Round;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record RoundNumberResponse(
        @Schema(example = "2")
        Long id,
        @Schema(example = "2")
        Integer number
) {

    public static RoundNumberResponse from(Round round) {
        return new RoundNumberResponse(round.getId(), round.getRoundNumber());
    }

    public static List<RoundNumberResponse> from(List<Round> rounds) {
        return rounds.stream()
                     .map(RoundNumberResponse::from)
                     .toList();
    }
}
