package com.yigongil.backend.request;

public record TodoUpdateRequest(
        Boolean isDone,
        String content
) {

}
