package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.roundofmember.RoundOfMemberRepository;
import com.yigongil.backend.exception.InvalidMemberInRoundException;
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
                .orElseThrow(() -> new RoundNotFoundException("해당 회차를 찾을 수 없습니다", roundId));

        List<RoundOfMember> roundOfMembers = roundOfMemberRepository.findRoundOfMembersWithMember(round.getRoundOfMembers());

        RoundOfMember roundByMember = roundOfMembers.stream()
                .filter(roundOfMember -> roundOfMember.isMemberEquals(member))
                .findAny()
                .orElseThrow(() -> new InvalidMemberInRoundException("해당 스터디에 존재하지 않는 회원입니다.", member.getId()));

        List<OptionalTodo> optionalTodos = roundByMember.getOptionalTodos();

        return new RoundResponse(
                roundId,
                round.getMaster().getId(),
                member.isSameWithMaster(round.getMaster()),
                TodoResponse.fromNecessaryTodo(roundByMember, round),
                TodoResponse.fromOptionalTodo(optionalTodos),
                MemberOfRoundResponse.from(roundOfMembers)
        );
    }
}
