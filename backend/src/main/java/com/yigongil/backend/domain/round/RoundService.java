package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.domain.study.Study;
import com.yigongil.backend.domain.study.StudyRepository;
import com.yigongil.backend.domain.studymember.StudyMember;
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
}
