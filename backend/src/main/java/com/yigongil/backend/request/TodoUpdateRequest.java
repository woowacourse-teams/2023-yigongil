package com.yigongil.backend.request;

import javax.validation.constraints.NotNull;

public record TodoUpdateRequest(
        @NotNull(message = "필수/선택 투두 여부는 필수로 입력되어야 합니다.")
        Boolean isNecessary,
        Boolean isDone,
        String content
) {

}
