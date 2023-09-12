package com.yigongil.backend.response;

import java.util.List;

public record FeedPostsResponse(
        List<CertificationFeedPostResponse> certifications,
        List<RegularFeedPostResponse> regulars
) {

        public static FeedPostsResponse of(
                List<CertificationFeedPostResponse> certifications,
                List<RegularFeedPostResponse> regulars
        ) {
            return new FeedPostsResponse(certifications, regulars);
        }
}
