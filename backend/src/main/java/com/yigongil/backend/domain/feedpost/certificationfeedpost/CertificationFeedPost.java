package com.yigongil.backend.domain.feedpost.certificationfeedpost;

import com.yigongil.backend.domain.feedpost.FeedPost;
import com.yigongil.backend.domain.round.Round;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class CertificationFeedPost extends FeedPost {

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    protected CertificationFeedPost() {
    }
}
