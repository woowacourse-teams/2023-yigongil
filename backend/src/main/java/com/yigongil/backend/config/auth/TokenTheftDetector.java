package com.yigongil.backend.config.auth;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenTheftDetector {

    private final Map<Long, String> upToDateTokens = new ConcurrentHashMap<>();

    public boolean isDetected(Long memberId, @NotNull String inputToken) {
        String upToDateToken = upToDateTokens.get(memberId);

        if (Objects.equals(upToDateToken, inputToken)) {
            return false;
        }
        upToDateTokens.remove(memberId);
        return true;
    }

    public void update(Long memberId, String token) {
        upToDateTokens.put(memberId, token);
    }
}
