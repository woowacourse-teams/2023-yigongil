package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record TodoUpdateRequest(
        @Schema(description = "투두 내용", example = "스웨거 오늘 안에 하기")
        String content
) {

}
