package com.yigongil.backend.domain.event;

public record StudyCreatedEvent(Long studyId, Long masterId) implements DomainEvent {

}
