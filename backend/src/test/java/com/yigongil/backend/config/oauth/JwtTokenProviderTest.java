package com.yigongil.backend.config.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    @Test
    void 토큰을_정상적으로_생성_파싱한다() {
        long expected = 5L;
        JwtTokenProvider tokenProvider = new JwtTokenProvider("testeststs23213124mfme13kf13etst", 1L);

        String token = tokenProvider.createToken(expected);
        Long actual = tokenProvider.parseToken(token);

        assertThat(actual).isEqualTo(expected);
    }
}
