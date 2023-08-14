package com.yigongil.backend.config.auth;

import com.yigongil.backend.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtTokenProvider {

    private static final String SCHEME = "Bearer ";

    private final SecretKey secretKey;
    private final Long expired;
    private final TokenTheftDetector tokenTheftDetector;

    public JwtTokenProvider(@Value("${jwt.key}") String secretKey, @Value("${jwt.expiration}") Long expired, TokenTheftDetector tokenTheftDetector) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expired = expired;
        this.tokenTheftDetector = tokenTheftDetector;
    }

    public String createAccessToken(Long id) {
        Instant issuedAt = Instant.now();
        return SCHEME + Jwts.builder()
                            .setExpiration(Date.from(issuedAt.plus(expired, ChronoUnit.MINUTES)))
                            .setIssuedAt(Date.from(issuedAt))
                            .setSubject(String.valueOf(id))
                            .signWith(secretKey, SignatureAlgorithm.HS256)
                            .compact();
    }

    public String createRefreshToken(Long id) {
        String refreshToken = Jwts.builder()
                                  .setId(UUID.randomUUID().toString())
                                  .setSubject(String.valueOf(id))
                                  .signWith(secretKey, SignatureAlgorithm.HS256)
                                  .compact();

        tokenTheftDetector.update(id, refreshToken);
        return SCHEME + refreshToken;
    }

    public void detectTokenTheft(String tokenWithScheme) {
        Long memberId = parseToken(tokenWithScheme);
        String token = tokenWithScheme.substring(SCHEME.length());

        if (tokenTheftDetector.isDetected(memberId, token)) {
            throw new InvalidTokenException("토큰이 탈취되었습니다. ", tokenWithScheme);
        }
    }

    public Long parseToken(String token) {
        if (!token.startsWith(SCHEME)) {
            throw new InvalidTokenException("유효하지 않은 토큰 타입입니다. 입력된 token: ", token);
        }
        String credentials = token.substring(SCHEME.length());
        JwtParser parser = Jwts.parserBuilder()
                               .setSigningKey(secretKey)
                               .build();
        Claims claims = parser.parseClaimsJws(credentials)
                              .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
