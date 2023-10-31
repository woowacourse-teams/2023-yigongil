package com.yigongil.backend.domain.event;

public record StudyStartedEvent(Long studyId, String studyName) implements DomainEvent {

}
