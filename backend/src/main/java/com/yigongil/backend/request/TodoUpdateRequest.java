package com.yigongil.backend.request;

public record TodoUpdateRequest(
        Boolean isNecessary,
        Boolean isDone,
        String content
) {

}
