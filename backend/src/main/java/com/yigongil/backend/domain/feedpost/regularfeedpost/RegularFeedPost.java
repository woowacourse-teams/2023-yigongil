package com.yigongil.backend.domain.feedpost.regularfeedpost;

import com.yigongil.backend.domain.feedpost.FeedPost;
import javax.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public class RegularFeedPost extends FeedPost {

    protected RegularFeedPost() {
    }
}
