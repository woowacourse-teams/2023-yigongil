package com.yigongil.backend.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

public record ReportCreateRequest(
        @Schema(description = "신고할 회원 id", example = "1")
        @Positive(message = "피신고자 식별자는 양수만 입력 가능합니다.")
        Long reportedMemberId,

        @Schema(description = "신고 제목", example = "신고합니다")
        @NotBlank(message = "신고 제목이 공백입니다.")
        String title,

        @Schema(description = "신고 내용", example = "스웨거 너무 힘듭니다")
        @NotBlank(message = "신고 내용이 공백입니다.")
        String content,

        @Schema(description = "문제 발생일", example = "2023.08.12")
        @PastOrPresent(message = "문제발생일이 미래의 날짜로 입력되었습니다.")
        LocalDate problemOccuredAt
) {

}
