package com.yigongil.backend.application;

import com.yigongil.backend.domain.round.DatePublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

    private final StudyService studyService;
    private final DatePublisher datePublisher;

    public ScheduleService(StudyService studyService, DatePublisher datePublisher) {
        this.studyService = studyService;
        this.datePublisher = datePublisher;
    }

    @Scheduled(cron = "${schedules.round-update}")
    @Transactional
    public void proceedRoundPerDay() {
        studyService.proceedRound(datePublisher.publish());
    }
}
