package com.yigongil.backend.request;

public record StudyCreateRequest(
        String name,
        Integer numberOfMaximumMembers,
        String startAt,
        String period,
        String periodOfRound,
        String introduction
) {
}
