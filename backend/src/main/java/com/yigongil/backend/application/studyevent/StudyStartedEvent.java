package com.yigongil.backend.application.studyevent;

import com.yigongil.backend.domain.study.Study;

public class StudyStartedEvent {

    private final Study study;

    public StudyStartedEvent(Study study) {
        this.study = study;
    }

    public Study getStudy() {
        return study;
    }
}
