package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.roundofmember.RoundOfMemberRepository;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.response.MemberOfRoundResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.TodoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final RoundOfMemberRepository roundOfMemberRepository;

    public RoundService(RoundRepository roundRepository, RoundOfMemberRepository roundOfMemberRepository) {
        this.roundRepository = roundRepository;
        this.roundOfMemberRepository = roundOfMemberRepository;
    }

    public RoundResponse findRoundDetail(Member member, Long roundId) {
        Round round = roundRepository.findRoundByIdWithRoundsOfMember(roundId)
                .orElseThrow(RoundNotFoundException::new);

        List<RoundOfMember> roundOfMembers = roundOfMemberRepository.findRoundOfMembersWithMember(round.getRoundOfMembers());

        RoundOfMember roundByMember = roundOfMembers.stream()
                .filter(roundOfMember -> roundOfMember.isMemberEquals(member))
                .findAny()
                .orElseThrow();
        //TODO: expception 머지 된후 수정

        List<OptionalTodo> optionalTodos = roundByMember.getOptionalTodos();

        return new RoundResponse(
                roundId,
                round.getMaster().getId(),
                TodoResponse.fromNecessaryTodo(roundByMember, round),
                TodoResponse.fromOptionalTodo(optionalTodos),
                MemberOfRoundResponse.from(roundOfMembers)
        );
    }
}
