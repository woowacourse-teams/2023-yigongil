package com.yigongil.backend.request;

public record TodoCreateRequest(
        Boolean isNecessary,
        Long roundId,
        String content
) {
}
