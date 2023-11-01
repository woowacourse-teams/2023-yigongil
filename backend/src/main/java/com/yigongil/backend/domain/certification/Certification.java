package com.yigongil.backend.domain.certification;

import com.yigongil.backend.domain.base.BaseEntity;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.round.Round;
import com.yigongil.backend.domain.study.Study;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Certification extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(length = 1000)
    private String content;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;

    protected Certification() {
    }

    @Builder
    public Certification(
            Long id,
            Member author,
            Study study,
            Round round,
            String content,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.author = author;
        this.study = study;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.round = round;
    }
}
