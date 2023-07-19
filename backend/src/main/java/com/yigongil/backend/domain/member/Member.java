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

    private Integer participatedStudyCount;

    private Integer finishedStudyCount;

    protected Member() {
    }

    @Builder
    public Member(
            Long id,
            String githubId,
            String nickname,
            String profileImageUrl,
            Integer tier,
            String introduction,
            Integer participatedStudyCount,
            Integer finishedStudyCount
    ) {
        this.id = id;
        this.githubId = githubId;
        this.nickname = new Nickname(nickname);
        this.profileImageUrl = profileImageUrl;
        this.tier = tier;
        this.introduction = new Introduction(introduction);
        this.participatedStudyCount = participatedStudyCount;
        this.finishedStudyCount = finishedStudyCount;
    }

    public void updateProfile(String nickname, String introduction) {
        this.nickname = new Nickname(nickname);
        this.introduction = new Introduction(introduction);
    }

    public Integer calculateSuccessRate() {
        if (Objects.isNull(finishedStudyCount) || Objects.isNull(participatedStudyCount)) {
            return 0;
        }
        return finishedStudyCount * 100 / participatedStudyCount;
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
