package com.yigongil.backend.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
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

    public JwtTokenProvider(@Value("${jwt.key}") String secretKey, @Value("${jwt.expiration}") Long expired) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expired = expired;
    }

    public String createToken(Long id) {
        Instant issuedAt = Instant.now();
        return SCHEME + Jwts.builder()
                            .setExpiration(Date.from(issuedAt.plus(expired, ChronoUnit.MINUTES)))
                            .setIssuedAt(Date.from(issuedAt))
                            .setSubject(String.valueOf(id))
                            .signWith(secretKey, SignatureAlgorithm.HS256)
                            .compact();
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

    public String renewToken(String previousToken) {
        String payload = previousToken.split("\\.")[1];
        try {
            String id = (String) new ObjectMapper().readValue(Base64.getUrlDecoder().decode(payload), Map.class)
                                                   .get("sub");
            return createToken(Long.valueOf(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
