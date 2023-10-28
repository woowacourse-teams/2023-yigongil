package com.yigongil.backend.domain.event;

public record MustDoUpdatedEvent(
        Long studyId,
        String studyName,
        String mustDo
) implements DomainEvent{
}
