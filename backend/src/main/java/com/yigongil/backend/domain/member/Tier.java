package com.yigongil.backend.domain.member;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Tier {

    BRONZE(10, 1),
    SILVER(30, 2),
    GOLD(60, 3),
    PLATINUM(100, 4),
    DIAMOND(150, 5),
    ;

    private final int upperBoundExperience;
    private final int order;

    Tier(int upperBoundExperience, int order) {
        this.upperBoundExperience = upperBoundExperience;
        this.order = order;
    }

    public static Tier getTier(int experience) {
        return Arrays.stream(Tier.values())
                     .filter(tier -> tier.isBiggerExperience(experience))
                     .findFirst()
                     .orElse(DIAMOND);
    }

    public int getPrevUpperBound() {
        return Arrays.stream(Tier.values())
                     .filter(tier -> tier.order == this.order - 1)
                     .map(tier -> tier.upperBoundExperience)
                     .findFirst()
                     .orElse(0);
    }

    public int calculateProgress(int currentExperience) {
        return (currentExperience - getPrevUpperBound()) * 100 / (upperBoundExperience - getPrevUpperBound());
    }

    public boolean isBiggerExperience(int experience) {
        return experience < this.upperBoundExperience;
    }
}
