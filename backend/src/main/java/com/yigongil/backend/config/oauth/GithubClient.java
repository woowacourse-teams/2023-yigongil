package com.yigongil.backend.config.oauth;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GithubClient {

    private final String id;
    private final String secret;
    private final String loginUri;

    public GithubClient(
            @Value("${oauth2.github.client.id}") String id,
            @Value("${oauth2.github.client.secret}") String secret,
            @Value("${oauth2.github.client.github-login-uri}") String loginUri
    ) {
        this.id = id;
        this.secret = secret;
        this.loginUri = loginUri;
    }
}
