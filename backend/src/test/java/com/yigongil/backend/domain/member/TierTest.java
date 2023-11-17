package com.yigongil.backend.domain.member;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.yigongil.backend.domain.member.domain.Tier;
import org.junit.jupiter.api.Test;

class TierTest {

    @Test
    void 경험치_별_티어를_계산한다() {

        assertAll(
                () -> assertEquals(Tier.BRONZE, Tier.getTier(0)),
                () -> assertEquals(Tier.BRONZE, Tier.getTier(9)),
                () -> assertEquals(Tier.SILVER, Tier.getTier(10)),
                () -> assertEquals(Tier.SILVER, Tier.getTier(29)),
                () -> assertEquals(Tier.GOLD, Tier.getTier(30)),
                () -> assertEquals(Tier.GOLD, Tier.getTier(59)),
                () -> assertEquals(Tier.PLATINUM, Tier.getTier(60)),
                () -> assertEquals(Tier.PLATINUM, Tier.getTier(99)),
                () -> assertEquals(Tier.DIAMOND, Tier.getTier(100)),
                () -> assertEquals(Tier.DIAMOND, Tier.getTier(149)),
                () -> assertEquals(Tier.DIAMOND, Tier.getTier(150)),
                () -> assertEquals(Tier.DIAMOND, Tier.getTier(151))
        );
    }
}
