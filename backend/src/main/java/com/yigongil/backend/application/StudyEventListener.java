package com.yigongil.backend.application;

import com.yigongil.backend.application.studyevent.StudyStartedEvent;
import com.yigongil.backend.domain.study.Study;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class StudyEventListener {

    private final RoundService roundservice;

    public StudyEventListener(RoundService roundservice) {
        this.roundservice = roundservice;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void listenStudyStartedEvent(StudyStartedEvent event) {
        Study study = event.getStudy();
        roundservice.updateRoundsEndAt(
                study.getRounds(),
                LocalDateTime.now(),
                study.getPeriodOfRound() * study.getPeriodUnit().getUnitNumber()
        );
    }
}
