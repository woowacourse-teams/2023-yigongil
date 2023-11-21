package com.yigongil.backend.domain.round;

import java.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

    private final RoundService roundService;

    public ScheduleService(RoundService roundService) {
        this.roundService = roundService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void proceedRoundPerDay() {
        roundService.proceedRound(LocalDate.now());
    }
}
