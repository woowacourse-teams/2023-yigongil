package com.yigongil.backend.domain.member;

import com.yigongil.backend.domain.base.BaseRootEntity;
import com.yigongil.backend.domain.event.MemberDeleteEvent;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;

@Getter
@SQLDelete(sql = Member.DELETE_QUERY)
@Entity
public class Member extends BaseRootEntity {

    protected static final String DELETE_QUERY = """
            update member
            set github_id = null,
            nickname = null,
            profile_image_url = null,
            introduction = null,
            deleted = true
            where id = ?
            """;
    private static final int MASTER_NUMBER = 0;
    private static final int PARTICIPANT_NUMBER = 1;
    private static final int MAXIMUM_TIER = 5;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true)
    private String githubId;

    @Embedded
    private Nickname nickname;

    private String profileImageUrl;

    @Column(nullable = false)
    private int experience;

    @Embedded
    private Introduction introduction;

    @Column(nullable = false)
    private boolean isOnboardingDone;

    @Column(nullable = false)
    private boolean deleted;

    protected Member() {
    }

    @Builder
    public Member(
            Long id,
            String githubId,
            String nickname,
            String profileImageUrl,
            int experience,
            String introduction,
            boolean isOnboardingDone,
            boolean deleted
    ) {
        this.id = id;
        this.githubId = githubId;
        this.nickname = new Nickname(nickname);
        this.profileImageUrl = profileImageUrl;
        this.experience = experience;
        this.introduction = new Introduction(introduction);
        this.isOnboardingDone = isOnboardingDone;
        this.deleted = deleted;
    }

    public void updateProfile(String nickname, String introduction) {
        this.nickname = new Nickname(nickname);
        this.introduction = new Introduction(introduction);
        this.isOnboardingDone = true;
    }

    public String getNickname() {
        if (this.nickname == null) {
            return null;
        }
        return nickname.getNickname();
    }

    public String getIntroduction() {
        if (introduction == null) {
            return null;
        }
        return introduction.getIntroduction();
    }

    @PreRemove
    public void registerDeleteEvent() {
        registerEvent(new MemberDeleteEvent(id));
    }

    public void addExperience(int exp) {
        this.experience += exp;
    }

    public int calculateProgress() {
        return getTier().calculateProgress(experience);
    }

    public Tier getTier() {
        return Tier.getTier(experience);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member member)) {
            return false;
        }
        return id.equals(member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
