package com.yigongil.backend.response;

import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;

import java.util.List;

public record TodoResponse(
        Long id,
        String content,
        Boolean isDone
) {

    public static TodoResponse fromNecessaryTodo(RoundOfMember roundByMember, Round round) {
        return new TodoResponse(
                roundByMember.getId(),
                round.getNecessaryToDoContent(),
                roundByMember.getDone()
        );
    }

    public static TodoResponse fromOptionalTodo(OptionalTodo optionalTodo) {
        return new TodoResponse(
                optionalTodo.getId(),
                optionalTodo.getContent(),
                optionalTodo.isDone()
        );
    }

    public static List<TodoResponse> fromOptionalTodo(List<OptionalTodo> optionalTodos) {
        return optionalTodos.stream()
                .map(TodoResponse::fromOptionalTodo)
                .toList();
    }
}
