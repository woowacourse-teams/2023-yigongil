package com.yigongil.backend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yigongil.backend.domain.feedpost.regularfeedpost.RegularFeedPost;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

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
        @Schema(example = "2021.08.12 12:00:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
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
