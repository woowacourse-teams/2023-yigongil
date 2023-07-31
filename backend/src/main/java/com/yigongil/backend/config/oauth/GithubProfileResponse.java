package com.yigongil.backend.config.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubProfileResponse(
        @JsonProperty("login") String githubId,
        @JsonProperty("avatar_url") String profileImageUrl
) {

}
