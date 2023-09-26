package com.yigongil.backend.domain.study;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class ProgressDayOfWeek {

    @EmbeddedId
    private ProgressDayOfWeekKey id;

    @ManyToOne
    @MapsId("studyId")  // This maps the studyId attribute of embedded id
    @JoinColumn(name = "study_id")
    private StudyV2 studyV2;

}


