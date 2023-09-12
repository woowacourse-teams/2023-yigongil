package com.yigongil.backend.response;

import com.yigongil.backend.domain.feedpost.regularfeedpost.RegularFeedPost;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegularFeedPostResponse(
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

    public static RegularFeedPostResponse from(RegularFeedPost regularFeedPost) {
        return new RegularFeedPostResponse(
                regularFeedPost.getId(),
                new SimpleMemberResponse(
                        regularFeedPost.getAuthor().getId(),
                        regularFeedPost.getAuthor().getNickname(),
                        regularFeedPost.getAuthor().getProfileImageUrl()
                ),
                regularFeedPost.getImageUrl(),
                regularFeedPost.getContent(),
                0,
                0,
                regularFeedPost.getCreatedAt()
        );
    }
}
