package com.yigongil.backend.config.oauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        if (token.startsWith(SCHEME)) {
            String credentials = token.substring(SCHEME.length());
            JwtParser parser = Jwts.parserBuilder()
                                   .setSigningKey(secretKey)
                                   .build();
            Claims claims = parser.parseClaimsJws(credentials)
                                .getBody();

            if (claims.getExpiration().before(Date.from(Instant.now()))) {
                throw new
            }
            return Long.parseLong(claims.getSubject());
        }
    }
}
