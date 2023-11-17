package com.yigongil.backend.domain.report;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.exception.InvalidReportException;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class MemberReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member reportedMember;

    protected MemberReport() {
    }

    @Builder
    public MemberReport(
            Long id,
            Member reporter,
            Member reportedMember,
            String title,
            String content,
            LocalDate problemOccurredAt
    ) {
        super(id, reporter, title, content, problemOccurredAt);
        validateReporterSameWithReported(reporter, reportedMember);
        this.reportedMember = reportedMember;
    }

    private void validateReporterSameWithReported(Member reporter, Member reported) {
        if (reporter.equals(reported)) {
            throw new InvalidReportException("신고자와 피신고자가 같은 회원입니다.", reporter.getId());
        }
    }
}
