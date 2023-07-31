package com.yigongil.backend.config.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubAccessToken(
        @JsonProperty("access_token") String accessToken,
        String scope,
        @JsonProperty("token_type") String tokenType
) {

}
