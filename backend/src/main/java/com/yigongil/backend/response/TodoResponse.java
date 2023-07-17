package com.yigongil.backend.response;

public record TodoResponse(
        Long id,
        String name,
        Boolean isDone
) {
}
