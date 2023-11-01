package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.event.MemberDeleteEvent;
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
    public void listenMemberDeleteEvent(MemberDeleteEvent event) {
        studyService.deleteByMasterId(event.memberId());
    }
}
