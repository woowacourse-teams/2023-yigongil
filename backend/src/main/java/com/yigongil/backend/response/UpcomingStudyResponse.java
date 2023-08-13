package com.yigongil.backend.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public record UpcomingStudyResponse(
        @Schema(example = "12")
        Long id,
        @Schema(example = "레스트 독스 스터디")
        String name,
        @Schema(example = "2")
        Long roundId,
        
        TodoResponse necessaryTodo,

        @Schema(example = "3")
        Integer leftDays,
        @Schema(example = "2023.08.15")
        LocalDate nextDate,
        @Schema(example = "50")
        Integer memberNecessaryTodoProgressRate,
        List<TodoResponse> optionalTodos
) {

}
