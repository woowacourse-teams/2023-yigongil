package com.yigongil.backend.domain.report;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.exception.InvalidReportContentLengthException;
import com.yigongil.backend.exception.InvalidReportTitleLengthException;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Entity
public abstract class Report extends BaseEntity {

    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 30;
    private static final int MAX_CONTENT_LENGTH = 200;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member reporter;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private LocalDate problemOccurredAt;

    protected Report() {
    }

    Report(
            Long id,
            Member reporter,
            String title,
            String content,
            LocalDate problemOccurredAt
    ) {
        validateTitleLength(title);
        validateContentLength(content);
        this.id = id;
        this.reporter = reporter;
        this.title = title;
        this.content = content;
        this.problemOccurredAt = problemOccurredAt;
    }

    private void validateTitleLength(String title) {
        if (title.length() < MIN_TITLE_LENGTH || title.length() > MAX_TITLE_LENGTH) {
            throw new InvalidReportTitleLengthException(
                    String.format(
                            "신고 제목은 %d자 이상 %d자 이하로 입력 가능합니다",
                            MIN_TITLE_LENGTH,
                            MAX_TITLE_LENGTH
                    ), String.valueOf(title.length())
            );
        }
    }

    private void validateContentLength(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidReportContentLengthException(
                    String.format(
                            "신고 내용은 %d자 이하로 입력 가능합니다",
                            MAX_CONTENT_LENGTH
                    ), String.valueOf(content.length())
            );
        }
    }
}
