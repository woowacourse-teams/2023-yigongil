package com.yigongil.backend.config.oauth;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    @Test
    void a() {
        long expiredMinute = 1L;
        long id = 5L;

        JwtTokenProvider tokenProvider = new JwtTokenProvider("testeststs23213124mfme13kf13etst", expiredMinute);
        String token = tokenProvider.createToken(id);

        JwtParser parser = Jwts.parserBuilder()
                               .setSigningKey(tokenProvider.getSecretKey())
                               .build();

        Claims claims = parser.parseClaimsJws(token)
                              .getBody();

        assertAll(
                () -> assertThat(claims.getSubject()).isEqualTo(valueOf(id)),
                () -> assertThat(claims.getIssuedAt().getTime() + Duration.ofMinutes(expiredMinute).toMillis())
                        .isEqualTo(claims.getExpiration().getTime())
        );
    }

}
