package com.yigongil.backend.response;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.study.Study;

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
    public static UpcomingStudyResponse of(Member member, Study study) {
        Round currentRound = study.getCurrentRound();
        RoundOfMember currentRoundInfo = study.findCurrentRoundOfMemberOwn(member);
        return new UpcomingStudyResponse(
                study.getId(),
                study.getName(),
                currentRound.getId(),
                TodoResponse.fromNecessaryTodo(currentRoundInfo, currentRound),
                1,
                "2023.02.01",
                study.findCurrentRoundOfMembers().calculateMembersProgress(),
                TodoResponse.fromOptionalTodo(currentRoundInfo.getOptionalTodos())
        );
    }

    public static List<UpcomingStudyResponse> of(Member member, List<Study> studies) {
        return studies.stream()
                .map(study -> UpcomingStudyResponse.of(member, study))
                .toList();
    }
}
