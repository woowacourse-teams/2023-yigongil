package com.yigongil.backend.application;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.round.RoundRepository;
import com.yigongil.backend.domain.roundofmember.RoundOfMember;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.exception.InvalidMemberInRoundException;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.response.MemberOfRoundResponse;
import com.yigongil.backend.response.ProgressRateResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.TodoResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoundService {

    private static final int MASTER_RESPONSE = 0;
    private static final int PARTICIPANT_RESPONSE = 1;
    private final RoundRepository roundRepository;
    private final StudyRepository studyRepository;

    public RoundService(
            RoundRepository roundRepository,
            StudyRepository studyRepository
    ) {
        this.roundRepository = roundRepository;
        this.studyRepository = studyRepository;
    }

    @Transactional(readOnly = true)
    public RoundResponse findRoundDetail(Member member, Long roundId) {
        Round round = findRoundById(roundId);

        List<RoundOfMember> roundOfMembers = round.getRoundOfMembers();

        RoundOfMember roundByMember =
                roundOfMembers.stream()
                              .filter(roundOfMember -> roundOfMember.isMemberEquals(member))
                              .findAny()
                              .orElseThrow(() -> new InvalidMemberInRoundException("해당 스터디에 존재하지 않는 회원입니다.", member.getId()));

        List<OptionalTodo> optionalTodos = roundByMember.getOptionalTodos();

        return new RoundResponse(
                roundId,
                round.getMaster().getId(),
                round.isMaster(member) ? MASTER_RESPONSE : PARTICIPANT_RESPONSE,
                TodoResponse.fromNecessaryTodo(roundByMember, round),
                TodoResponse.fromOptionalTodo(optionalTodos),
                MemberOfRoundResponse.from(roundOfMembers)
        );
    }

    @Transactional(readOnly = true)
    public List<UpcomingStudyResponse> findCurrentRoundOfStudies(Member member) {
        List<Study> studies = studyRepository.findByMemberAndProcessingStatus(member, ProcessingStatus.PROCESSING);
        List<UpcomingStudyResponse> upcomingStudyResponses = new ArrayList<>();

        for (Study study : studies) {
            Round currentRound = study.getCurrentRound();

            LocalDateTime endAt = currentRound.getEndAt();
            int leftDays = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), endAt) + PARTICIPANT_RESPONSE;

            upcomingStudyResponses.add(
                    new UpcomingStudyResponse(
                            study.getId(),
                            study.getName(),
                            currentRound.getNecessaryToDoContent(),
                            leftDays,
                            currentRound.calculateProgress(),
                            study.isMaster(member)
                    )
            );
        }

        return upcomingStudyResponses;
    }

    @Transactional(readOnly = true)
    public ProgressRateResponse findProgressRateByRound(Long roundId) {
        Round round = findRoundById(roundId);
        return new ProgressRateResponse(round.calculateProgress());
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(() -> new RoundNotFoundException("해당 회차를 찾을 수 없습니다", roundId));
    }
}
