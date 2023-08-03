package com.yigongil.backend.domain.member;

import com.yigongil.backend.domain.BaseEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Member extends BaseEntity {

    private static final int MASTER_NUMBER = 0;
    private static final int PARTICIPANT_NUMBER = 1;

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
        this.tier = tier != null ? tier : 1;
        this.introduction = new Introduction(introduction);
    }

    public void updateProfile(String nickname, String introduction) {
        this.nickname = new Nickname(nickname);
        this.introduction = new Introduction(introduction);
    }

    public String getNickname() {
        return nickname.getNickname();
    }

    public String getIntroduction() {
        return introduction.getIntroduction();
    }

    public int isSameWithMaster(Member master) {
        if (this.equals(master)) {
            return MASTER_NUMBER;
        }
        return PARTICIPANT_NUMBER;
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
