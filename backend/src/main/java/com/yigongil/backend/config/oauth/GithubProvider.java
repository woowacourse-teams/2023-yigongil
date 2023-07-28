package com.yigongil.backend.config.oauth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GithubProvider {

    private final String tokenUrl;
    private final String userInfoUrl;

    public GithubProvider(
            @Value("${oauth2.github.provider.token-uri}") String tokenUrl,
            @Value("${oauth2.github.provider.user-info-uri}")String userInfoUrl
    ) {
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
    }
}
