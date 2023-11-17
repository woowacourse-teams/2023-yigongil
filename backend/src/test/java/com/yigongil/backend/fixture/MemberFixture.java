package com.yigongil.backend.fixture;

import com.yigongil.backend.domain.member.domain.Member;

public enum MemberFixture {

    김진우(1L, "jwkim", "김진우", "www.123.com", "자기소개입니다"),
    폰노이만(2L, "noiman", "폰뇌만", "www.456.com", "I'm Noiman Pawn"),
    마틴파울러(3L, "fowler", "파울장인", "www.789.com", "파울");

    private final Long id;
    private final String githubId;
    private final String nickname;
    private final String profileImageUrl;
    private final String introduction;

    MemberFixture(Long id, String githubId, String nickname, String profileImageUrl, String introduction) {
        this.id = id;
        this.githubId = githubId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.introduction = introduction;
    }

    public Member toMember() {
        return Member.builder()
                     .id(id)
                     .githubId(githubId)
                     .nickname(nickname)
                     .profileImageUrl(profileImageUrl)
                     .introduction(introduction)
                     .build();
    }
}
