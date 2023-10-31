package com.yigongil.backend.domain.event;

public record MemberDeletedEvent(Long memberId) implements DomainEvent {

}
