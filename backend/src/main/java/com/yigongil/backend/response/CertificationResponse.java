package com.yigongil.backend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yigongil.backend.domain.certification.Certification;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record CertificationResponse(
        @Schema(example = "1")
        Long id,
        SimpleMemberResponse author,
        @Schema(example = "http://image.png")
        String imageUrl,
        @Schema(example = "진우는 모든 것을 잘합니다")
        String content,
        @Schema(example = "2021.08.12 12:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {

    public static CertificationResponse from(Certification certification) {
        return new CertificationResponse(
                certification.getId(),
                new SimpleMemberResponse(
                        certification.getAuthor().getId(),
                        certification.getAuthor().getNickname(),
                        certification.getAuthor().getProfileImageUrl()
                ),
                certification.getImageUrl(),
                certification.getContent(),
                certification.getCreatedAt()
        );
    }
}
