package com.yigongil.backend.domain.study;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ProgressDayOfWeekKey implements Serializable {

    @Column(name = "study_id")
    private Long studyId;

    @Column(name = "progress_day_of_week")
    private int progressDayOfWeek;

}
