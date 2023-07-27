package com.yigongil.backend.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ScheduleService {

    private final StudyService studyService;
    private final LocalDate roundUpdateDate;

    public ScheduleService(StudyService studyService, LocalDate roundUpdateDate) {
        this.studyService = studyService;
        this.roundUpdateDate = roundUpdateDate;
    }

    @Scheduled(cron = "${schedules.round-update}")
    @Transactional
    public void proceedRoundPerDay() {
        studyService.proceedRound(roundUpdateDate);
    }
}
