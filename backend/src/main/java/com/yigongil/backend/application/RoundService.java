package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.roundofmember.RoundOfMemberRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMembers;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.InvalidMemberInRoundException;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.response.HomeResponse;
import com.yigongil.backend.response.MemberOfRoundResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.TodoResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final RoundOfMemberRepository roundOfMemberRepository;
    private final StudyRepository studyRepository;

    public RoundService(RoundRepository roundRepository, RoundOfMemberRepository roundOfMemberRepository, StudyRepository studyRepository) {
        this.roundRepository = roundRepository;
        this.roundOfMemberRepository = roundOfMemberRepository;
        this.studyRepository = studyRepository;
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

    public HomeResponse findCurrentRoundOfStudies(Member member) {
        List<Study> studies = studyRepository.findByMember(member);
        List<UpcomingStudyResponse> upcomingStudyResponses = new ArrayList<>();

        for (Study study : studies) {
            Round currentRound = study.getCurrentRound();
            RoundOfMember currentRoundOfMemberOwn = study.findCurrentRoundOfMemberBy(member);
            RoundOfMembers currentRoundOfMembers = study.findCurrentRoundOfMembers();

            upcomingStudyResponses.add(
                    new UpcomingStudyResponse(
                            study.getId(),
                            study.getName(),
                            currentRound.getId(),
                            TodoResponse.fromNecessaryTodo(currentRoundOfMemberOwn, currentRound),
                            5,
                            "2023.07.26",
                            currentRoundOfMembers.calculateMembersProgress(),
                            TodoResponse.fromOptionalTodo(currentRoundOfMemberOwn.getOptionalTodos())
                    )
            );
        }

        return HomeResponse.of(member, studies, upcomingStudyResponses);
    }
}
