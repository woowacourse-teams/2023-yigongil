package com.yigongil.backend.response;

import java.util.List;

public record RoundResponse(
        Long id,
        Long masterId,
        TodoResponse necessaryTodo,
        List<TodoResponse> optionalTodos,
        List<MemberOfRoundResponse> members
) {
}
