package com.yigongil.backend.domain.report;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.study.Study;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class StudyReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    private Study reportedStudy;

    protected StudyReport() {
    }

    @Builder
    public StudyReport(
            Long id,
            Member reporter,
            Study reportedStudy,
            String title,
            String content,
            LocalDate problemOccurredAt
    ) {
        super(id, reporter, title, content, problemOccurredAt);
        this.reportedStudy = reportedStudy;
    }
}
