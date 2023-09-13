package com.yigongil.backend.domain.feedpost.regularfeedpost;

import com.yigongil.backend.domain.feedpost.FeedPost;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.study.Study;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class RegularFeedPost extends FeedPost {

    protected RegularFeedPost() {
    }

    @Builder
    public RegularFeedPost(
            Long id,
            Member author,
            Study study,
            String content,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        super(
                id,
                author,
                study,
                content,
                imageUrl,
                createdAt
        );
    }
}
