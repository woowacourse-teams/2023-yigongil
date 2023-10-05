package com.yigongil.backend.domain.study;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;

@Getter
@Embeddable
public class ProgressDayOfWeekKey implements Serializable {

    private Long studyId;

    @Enumerated(EnumType.STRING)
    private DayOfWeek progressDayOfWeek;

}
