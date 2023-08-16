package com.yigongil.backend.config.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yigongil.backend.exception.AuthorizationException;
import org.junit.jupiter.api.Test;

class TokenTheftDetectorTest {

    @Test
    void isDetected_입력이_null이면_예외를_던진다() {
        TokenTheftDetector tokenTheftDetector = new TokenTheftDetector();
        tokenTheftDetector.update(1L, "ey.ey.ey");

        assertThatThrownBy(() -> tokenTheftDetector.isDetected(1L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isDetected_토큰이_저장되어_있지_않으면_예외를_던진다() {
        TokenTheftDetector tokenTheftDetector = new TokenTheftDetector();

        assertThatThrownBy(() -> tokenTheftDetector.isDetected(1L, "null"))
                .isInstanceOf(AuthorizationException.class);
    }
}
