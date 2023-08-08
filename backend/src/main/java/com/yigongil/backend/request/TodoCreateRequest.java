package com.yigongil.backend.request;

import javax.validation.constraints.NotBlank;

public record TodoCreateRequest(
        @NotBlank(message = "투두 내용이 비었습니다.")
        String content
) {

}
