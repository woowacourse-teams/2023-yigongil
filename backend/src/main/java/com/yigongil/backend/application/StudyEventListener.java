package com.yigongil.backend.application;

import com.yigongil.backend.domain.event.MemberDeletedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StudyEventListener {

    private final StudyService studyService;

    public StudyEventListener(StudyService studyService) {
        this.studyService = studyService;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void listenMemberDeleteEvent(MemberDeletedEvent event) {
        studyService.deleteByMasterId(event.memberId());
    }
}
