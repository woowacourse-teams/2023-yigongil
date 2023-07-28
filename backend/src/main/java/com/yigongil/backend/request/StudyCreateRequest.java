package com.yigongil.backend.request;

public record StudyCreateRequest(
        String name,
        Integer numberOfMaximumMembers,
        String startAt,
        Integer totalRoundCount,
        String periodOfRound,
        String introduction
) {

}
