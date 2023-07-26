package com.yigongil.backend.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

    private final StudyService studyService;

    public ScheduleService(StudyService studyService) {
        this.studyService = studyService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void proceedRoundPerDay() {
        studyService.proceedRound();
    }
}
