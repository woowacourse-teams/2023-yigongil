package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public record TodoCreateRequest(
        @Schema(description = "투두 내용", example = "오늘안에 스웨거 만들기")
        @NotBlank(message = "투두 내용이 비었습니다.")
        String content
) {

}
