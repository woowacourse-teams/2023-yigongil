package com.yigongil.backend.response;

import com.yigongil.backend.domain.feedpost.certificationfeedpost.CertificationFeedPost;
import io.swagger.v3.oas.annotations.media.Schema;

public record CertificationFeedPostResponse(
        @Schema(example = "1")
        Long id,
        SimpleMemberResponse author,
        @Schema(example = "http://image.png")
        String imageUrl,
        @Schema(example = "진우는 모든 것을 잘합니다")
        String content,
        @Schema(example = "5")
        Integer likeCount,
        @Schema(example = "3")
        Integer commentCount,
        @Schema(example = "2021.08.12T12:00:00")
        java.time.LocalDateTime createdAt
) {

    public static CertificationFeedPostResponse from(CertificationFeedPost certificationFeedPost) {
        return new CertificationFeedPostResponse(
                certificationFeedPost.getId(),
                new SimpleMemberResponse(
                        certificationFeedPost.getAuthor().getId(),
                        certificationFeedPost.getAuthor().getNickname(),
                        certificationFeedPost.getAuthor().getProfileImageUrl()
                ),
                certificationFeedPost.getImageUrl(),
                certificationFeedPost.getContent(),
                0,
                0,
                certificationFeedPost.getCreatedAt()
        );
    }
}
