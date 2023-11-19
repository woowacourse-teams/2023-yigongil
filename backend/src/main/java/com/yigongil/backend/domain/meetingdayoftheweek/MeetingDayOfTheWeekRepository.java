package com.yigongil.backend.domain.meetingdayoftheweek;

import org.springframework.data.repository.Repository;

public interface MeetingDayOfTheWeekRepository extends Repository<MeetingDayOfTheWeek, Long> {

    MeetingDayOfTheWeek save(MeetingDayOfTheWeek meetingDayOfTheWeek);

}
