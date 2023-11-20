package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.domain.studymember.StudyMember;
import com.yigongil.backend.exception.RoundNotFoundException;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            Round currentRound = study.getCurrentRound();
            StudyMember studyMember = study.getStudyMemberByMember(member);

            int leftDays = currentRound.calculateLeftDaysFrom(LocalDate.now());

            upcomingStudyResponses.add(
                    new UpcomingStudyResponse(
                            study.getId(),
                            study.getName(),
                            currentRound.getMustDo(),
                            leftDays,
                            studyMember.calculateAccumulatedExperience(),
                            study.isMaster(member)
                    )
            );
        }

        return upcomingStudyResponses;
    }

    @Transactional
    public void updateMustDo(Member member, Long roundId, MustDoUpdateRequest request) {
        Round round = findRoundById(roundId);
        round.updateMustDo(member, request.content());
    }

    private Round findRoundById(Long roundId) {
        return roundRepository.findById(roundId)
                              .orElseThrow(() -> new RoundNotFoundException("존재하지 않는 회차입니다.", roundId));
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
        List<Study> studies = studyRepository.findAllByProcessingStatus(ProcessingStatus.PROCESSING);

        studies.stream()
               .filter(study -> study.isCurrentRoundEndAt(today.minusDays(1)))
               .forEach(Study::updateToNextRound);
    }
}
