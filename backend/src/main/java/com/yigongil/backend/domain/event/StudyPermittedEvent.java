package com.yigongil.backend.domain.event;

public record StudyPermittedEvent(Long permittedMemberId, Long studyId, String studyName) implements DomainEvent {

}
