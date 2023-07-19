package com.yigongil.backend.domain.member;

import com.yigongil.backend.domain.BaseEntity;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Member extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String githubId;

    @Embedded
    private Nickname nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private Integer tier;

    @Embedded
    private Introduction introduction;

    protected Member() {
    }

    @Builder
    public Member(
            Long id,
            String githubId,
            String nickname,
            String profileImageUrl,
            Integer tier,
            String introduction
    ) {
        this.id = id;
        this.githubId = githubId;
        this.nickname = new Nickname(nickname);
        this.profileImageUrl = profileImageUrl;
        this.tier = tier;
        this.introduction = new Introduction(introduction);
    }

    public void updateProfile(String nickname, String introduction) {
        this.nickname = new Nickname(nickname);
        this.introduction = new Introduction(introduction);
    }

    public Long getId() {
        return id;
    }

    public String getGithubId() {
        return githubId;
    }

    public String getNickname() {
        return nickname.getNickname();
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Integer getTier() {
        return tier;
    }

    public String getIntroduction() {
        return introduction.getIntroduction();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
