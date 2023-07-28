package com.yigongil.backend.config.oauth;

public record GithubAccessToken(
        String accessToken,
        String scope,
        String tokenType
) {

}
