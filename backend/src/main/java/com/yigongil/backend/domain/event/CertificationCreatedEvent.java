package com.yigongil.backend.domain.event;

public record CertificationCreatedEvent(
        Long studyId,
        String studyName,
        String authorGithubId
) {
}
