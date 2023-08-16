package com.yigongil.backend.config.auth;

import com.yigongil.backend.exception.AuthorizationException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenTheftDetector {

    private final Map<Long, String> storedTokens = new ConcurrentHashMap<>();

    public boolean isDetected(Long memberId, String inputToken) {
        String storedToken = storedTokens.get(memberId);

        validateNotNull(storedToken, inputToken);

        if (storedToken.equals(inputToken)) {
            return false;
        }
        storedTokens.remove(memberId);
        return true;
    }

    private void validateNotNull(String storedToken, String inputToken) {
        if (inputToken == null) {
            throw new IllegalArgumentException("입력 토큰은 null일 수 없습니다");
        }
        if (storedToken == null) {
            throw new AuthorizationException("로그인 이력이 없습니다. 로그인 정보: ", null);
        }
    }

    public void update(Long memberId, String token) {
        storedTokens.put(memberId, token);
    }
}
