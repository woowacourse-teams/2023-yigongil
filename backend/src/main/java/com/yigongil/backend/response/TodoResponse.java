package com.yigongil.backend.response;

import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import io.swagger.v3.oas.annotations.media.Schema;

public record TodoResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "오늘은 꼭 양치를 할거야")
        String content,
        @Schema(example = "false")
        Boolean isDone
) {

    public static TodoResponse fromNecessaryTodo(RoundOfMember roundByMember, Round round) {
        return new TodoResponse(
                roundByMember.getId(),
                round.getNecessaryToDoContent(),
                roundByMember.isDone()
        );
    }
}
