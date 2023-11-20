package com.yigongil.backend.domain.study;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record StudyStartedEvent(
    Long studyId,
    List<DayOfWeek> dayOfWeeks,
    LocalDate startAt
) {

}
