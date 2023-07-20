package com.yigongil.backend.response;

import java.util.List;

public record UpcomingStudyResponse(
        Long id,
        String name,
        Long roundId,
        TodoResponse necessaryTodo,
        Integer leftDays,
        String nextDate,
        Integer memberNecessaryTodoProgressRate,
        List<TodoResponse> optionalTodos
) {
}
