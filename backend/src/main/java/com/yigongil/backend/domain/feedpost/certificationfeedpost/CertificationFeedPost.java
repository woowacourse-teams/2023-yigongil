package com.yigongil.backend.domain.feedpost.certificationfeedpost;

import com.yigongil.backend.domain.feedpost.FeedPost;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class CertificationFeedPost extends FeedPost {

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    protected CertificationFeedPost() {
    }

    @Builder
    public CertificationFeedPost(
            Long id,
            Member author,
            Study study,
            Round round,
            String content,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        super(id, author, study, content, imageUrl, createdAt);
        this.round = round;
    }
}
