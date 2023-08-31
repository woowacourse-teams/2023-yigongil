package com.yigongil.backend.application;

import com.yigongil.backend.domain.event.MemberDeleteEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class StudyEventListener {

    private final StudyService studyService;

    public StudyEventListener(StudyService studyService) {
        this.studyService = studyService;
    }

    @TransactionalEventListener
    public void listenMemberDeleteEvent(MemberDeleteEvent event) {
        studyService.deleteStudyByMasterId(event.memberId());
        studyService.removeDeletedMemberFromRecruitingStudy(event.memberId());
    }
}
