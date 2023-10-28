package com.yigongil.backend.domain.event;

public record StudyAppliedEvent(Long studyMasterId, String studyName, String appliedMemberGithubId) implements DomainEvent {

}
