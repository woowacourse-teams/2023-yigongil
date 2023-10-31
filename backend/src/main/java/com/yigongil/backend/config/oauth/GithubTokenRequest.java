package com.yigongil.backend.config.oauth;


import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubTokenRequest(
        @JsonProperty("client_id") String clientId,
        @JsonProperty("client_secret") String clientSecret,
        String code
) {

}
