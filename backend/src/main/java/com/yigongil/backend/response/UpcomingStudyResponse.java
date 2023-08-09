package com.yigongil.backend.response;

import java.time.LocalDate;
import java.util.List;

public record UpcomingStudyResponse(
        Long id,
        String name,
        Long roundId,
        TodoResponse necessaryTodo,
        Integer leftDays,
        LocalDate nextDate,
        Integer memberNecessaryTodoProgressRate,
        List<TodoResponse> optionalTodos
) {

}
