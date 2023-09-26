package com.yigongil.backend.domain.study;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class StudyV2 extends Study {

    @Column(nullable = false)
    private int minimumWeeks;
}
