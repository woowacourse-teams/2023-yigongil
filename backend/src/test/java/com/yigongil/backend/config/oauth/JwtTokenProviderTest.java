package com.yigongil.backend.config.oauth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.config.auth.TokenTheftDetector;
import com.yigongil.backend.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JwtTokenProviderTest {

    private final JwtTokenProvider tokenProvider = new JwtTokenProvider("testeststs23213124mfme13kf13etst", 1L, new TokenTheftDetector());

    @Test
    void 토큰을_정상적으로_생성_파싱한다() {
        long expected = 5L;

        String token = tokenProvider.createAccessToken(expected);
        Long actual = tokenProvider.parseToken(token);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 리프레시_토큰이_최근에_발급된_것이면_예외를_던지지_않는다() {
        String refreshToken = tokenProvider.createRefreshToken(1L);

        assertAll(
                () -> assertDoesNotThrow(() -> tokenProvider.detectTokenTheft(refreshToken))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 100})
    void 동일한_리프레시_토큰을_두번_이상_사용하면_탈취를_감지한다(int count) {
        String refreshToken = tokenProvider.createRefreshToken(1L);

        for (int i = 0; i < count - 1; i++) {
            tokenProvider.createRefreshToken(1L);
        }

        assertAll(
                () -> assertThatThrownBy(() -> tokenProvider.detectTokenTheft(refreshToken))
                        .isInstanceOf(InvalidTokenException.class)
        );
    }

    @Test
    void 발급되지_않은_리프레시_토큰을_사용하면_탈취를_감지한다() {
        String refreshToken = Jwts.builder()
                                  .setId(UUID.randomUUID().toString())
                                  .setSubject(String.valueOf(1L))
                                  .signWith(tokenProvider.getSecretKey(), SignatureAlgorithm.HS256)
                                  .compact();

        assertAll(
                () -> assertThatThrownBy(() -> tokenProvider.detectTokenTheft(refreshToken))
                        .isInstanceOf(InvalidTokenException.class)
        );
    }

    @Test
    void 토큰이_null이면_예외를_던진다() {
        assertAll(
                () -> assertThatThrownBy(() -> tokenProvider.detectTokenTheft(null))
                        .isInstanceOf(InvalidTokenException.class)
        );
    }
}
