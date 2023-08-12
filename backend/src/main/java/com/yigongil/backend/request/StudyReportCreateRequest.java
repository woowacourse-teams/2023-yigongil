package com.yigongil.backend.request;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

public record StudyReportCreateRequest(
        @Positive(message = "신고할 스터디의 식별자는 양수만 입력 가능합니다.")
        Long reportedStudyId,
        @NotBlank(message = "신고 제목이 공백입니다.")
        String title,
        @NotBlank(message = "신고 내용이 공백입니다.")
        String content,
        @PastOrPresent(message = "문제발생일이 미래의 날짜로 입력되었습니다.")
        LocalDate problemOccurredAt
) {

}
