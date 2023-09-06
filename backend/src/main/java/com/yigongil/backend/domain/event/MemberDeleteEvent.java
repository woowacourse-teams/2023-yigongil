package com.yigongil.backend.domain.event;

public record MemberDeleteEvent(Long memberId) implements DomainEvent {

}
