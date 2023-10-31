package com.yigongil.backend.domain.event;

import java.time.LocalDateTime;

public record FeedPostCreatedEvent
        (
                Long studyId,
                String studyName,
                String authorGithubId,
                String content,
                String imageUrl,
                LocalDateTime createdAt
        ) implements DomainEvent {

}
