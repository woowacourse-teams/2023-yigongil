package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record RoundResponse(
        @Schema(example = "4")
        Long id,
        @Schema(example = "2")
        Long masterId,
        @Schema(example = "0")
        Integer role,
        TodoResponse necessaryTodo,
        List<TodoResponse> optionalTodos,
        List<MemberOfRoundResponse> members
) {

}
