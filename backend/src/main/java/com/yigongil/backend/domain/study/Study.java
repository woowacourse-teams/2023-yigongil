package com.yigongil.backend.domain.study;

import com.yigongil.backend.domain.BaseEntity;
import com.yigongil.backend.domain.round.Round;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Study extends BaseEntity {

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private int maximumNumberOfMembers;

    @Column(nullable = false)
    private int processingStatus;

    @Column(nullable = false)
    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Column(nullable = false)
    private int totalPeriod;

    @Column(nullable = false)
    private int roundPeriod;
    // TODO: 2023/07/14 Period 객체 생성 (사용자가 선택한 단위, int 형태의 기간 저장) 

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Round currentRound;

    @OneToMany
    @JoinColumn(name = "study_id", nullable = false)
    private List<Round> rounds = new ArrayList<>();

    protected Study() {
    }
}
