package com.yigongil.backend.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record TodoCreateRequest(
        @NotNull(message = "필수/선택 투두 여부는 필수로 입력되어야 합니다.")
        Boolean isNecessary,
        @Positive(message = "라운드 식별자는 음수일 수 없습니다.")
        Long roundId,
        @NotBlank(message = "투두 내용이 비었습니다.")
        String content
) {

}
