package com.yigongil.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Member extends BaseEntity {

    private static final int MAX_NICKNAME_LENGTH = 8;
    private static final int MAX_INTRODUCTION_LENGTH = 200;

    @Column(nullable = false, unique = true)
    private String githubId;

    @Column(nullable = false, unique = true, length = MAX_NICKNAME_LENGTH)
    private String nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private int tier;

    @Column(nullable = false, length = MAX_INTRODUCTION_LENGTH)
    private String introduction;

    protected Member() {
    }
}
