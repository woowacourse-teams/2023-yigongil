package com.yigongil.backend.config.oauth;


public record GithubTokenRequest(
        String clientId,
        String clientSecret,
        String code,
        String redirectUrl
) {

}
