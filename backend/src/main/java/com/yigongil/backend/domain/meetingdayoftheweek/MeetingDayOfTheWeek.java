package com.yigongil.backend.domain.meetingdayoftheweek;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.study.Study;
import java.time.DayOfWeek;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class MeetingDayOfTheWeek extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @Enumerated(EnumType.ORDINAL)
    private DayOfWeek dayOfWeek;

    protected MeetingDayOfTheWeek() {
    }

    @Builder
    public MeetingDayOfTheWeek(Long id, Study study, DayOfWeek dayOfWeek) {
        this.id = id;
        this.study = study;
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isSameDayOfWeek(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.equals(dayOfWeek);
    }

    public boolean comesNext(DayOfWeek dayOfWeek) {
        return this.dayOfWeek.compareTo(dayOfWeek) > 0;
    }

    public int getOrder() {
        return this.dayOfWeek.getValue();
    }
}


