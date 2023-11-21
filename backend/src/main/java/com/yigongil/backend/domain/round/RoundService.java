package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyExitedEvent;
import com.yigongil.backend.domain.study.StudyFinishedEvent;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.domain.study.StudyStartedEvent;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class RoundService {

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
    public List<UpcomingStudyResponse> findCurrentRoundOfStudies(Member member) {
        List<Study> studies = studyRepository.findByMemberAndProcessingStatus(member, ProcessingStatus.PROCESSING);
        List<UpcomingStudyResponse> upcomingStudyResponses = new ArrayList<>();

        for (Study study : studies) {
            Round currentRound = roundRepository.getByStudyIdAndRoundStatus(study.getId(), RoundStatus.IN_PROGRESS);

            int leftDays = currentRound.calculateLeftDaysFrom(LocalDate.now());

            upcomingStudyResponses.add(
                new UpcomingStudyResponse(
                    study.getId(),
                    study.getName(),
                    currentRound.getMustDo(),
                    leftDays,
                    calculateExperience(study.getId(), study.getRoundExperience()).get(member),
                    study.isMaster(member)
                )
            );
        }

        return upcomingStudyResponses;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void addExperience(StudyFinishedEvent event) {
        Map<Member, Integer> memberToExperience = calculateExperience(event.studyId(), event.roundExperience());

        for (Member member : memberToExperience.keySet()) {
            member.addExperience(memberToExperience.get(member));
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void exit(StudyExitedEvent event) {
        List<Round> rounds = roundRepository.findAllByStudyId(event.studyId());
        for (Round round : rounds) {
            round.exit(event.memberId());
        }
    }

    private Map<Member, Integer> calculateExperience(Long studyId, Integer roundExperience) {
        List<Round> rounds = roundRepository.findAllByStudyId(studyId);

        Map<Member, Integer> memberToExperience = new HashMap<>();
        for (Round round : rounds) {
             round.calculateExperience(roundExperience)
                  .forEach(((member, experience) -> memberToExperience.merge(member, experience, Integer::sum)));
        }
        return memberToExperience;
    }

    @Transactional
    public void updateMustDo(Member member, Long roundId, MustDoUpdateRequest request) {
        Round round = findRoundById(roundId);
        round.updateMustDo(member, request.content());
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(
                                  () -> new RoundNotFoundException("존재하지 않는 회차입니다.", roundId));
    }

    @Transactional(readOnly = true)
    public List<RoundResponse> findRoundDetailsOfWeek(Long id, Integer weekNumber) {
        List<Round> roundsOfWeek = roundRepository.findAllByStudyIdAndWeekNumber(id, weekNumber);
        return roundsOfWeek.stream()
                           .map(RoundResponse::from)
                           .toList();
    }

    @Transactional
    public void proceedRound(LocalDate today) {
        List<Round> rounds = roundRepository.findByRoundStatusAndDayOfWeek(
            RoundStatus.IN_PROGRESS,
            today.minusDays(1).getDayOfWeek()
        );

        for (Round round : rounds) {
            Integer currentWeek = round.getWeekNumber();
            List<Round> upcomingCandidates = roundRepository.findAllByStudyIdAndWeekNumberIn(
                round.getStudy().getId(),
                List.of(currentWeek, currentWeek + 1)
            );

            Round upcomingRound = upcomingCandidates.stream()
                              .filter(candidate -> candidate.isSameWeek(currentWeek) && candidate.isNotStarted())
                              .min(Comparator.comparing(Round::getDayOfWeek))
                              .orElseGet(() -> upcomingCandidates.stream()
                                                                 .filter(candidate -> candidate.isSameWeek(currentWeek + 1))
                                                                 .min(Comparator.comparing(Round::getDayOfWeek))
                                                                 .orElseThrow(() -> new IllegalStateException("다음 주 라운드를 안 만들어놓음")));

            round.finish();
            upcomingRound.proceed();

            if (!upcomingRound.isSameWeek(currentWeek)) {
                List<Round> nextWeekRounds = upcomingCandidates.stream()
                                                      .filter(candidate -> candidate.isSameWeek(currentWeek + 1))
                                                      .map(Round::createNextWeekRound)
                                                      .toList();

                roundRepository.saveAll(nextWeekRounds);
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void initializeRounds(StudyStartedEvent event) {
        Study study = studyRepository.getById(event.studyId());
        List<Round> rounds = new ArrayList<>();
        rounds.addAll(createRoundsOfFirstWeek(study, event.dayOfWeeks(), event.startAt()));
        rounds.addAll(createRoundsOf(2, study, event.dayOfWeeks()));
        rounds.get(0).proceed();
        for (Round round : rounds) {
            roundRepository.save(round);
        }
    }

    private List<Round> createRoundsOfFirstWeek(Study study, List<DayOfWeek> dayOfWeeks, LocalDate startAt) {
        List<Round> rounds = dayOfWeeks.stream()
                                       .filter(dayOfWeek -> dayOfWeek.compareTo(startAt.getDayOfWeek()) > 0)
                                       .map(dayOfWeek -> Round.of(dayOfWeek, study, 1))
                                       .toList();
        if (rounds.isEmpty()) {
            rounds = createRoundsOf(1, study, dayOfWeeks);
        }
        return rounds;
    }

    private List<Round> createRoundsOf(Integer weekNumber, Study study, List<DayOfWeek> dayOfWeeks) {
        return dayOfWeeks.stream()
                         .map(dayOfWeek -> Round.of(dayOfWeek, study, weekNumber))
                         .toList();
    }
}
