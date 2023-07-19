package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.Round;

import java.util.List;

public record RoundNumberResponse(
        Long id,
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
