package com.yigongil.backend.config.oauth;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Getter
@Component
public class GithubOauth {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final GithubProvider githubProvider;
    private final GithubClient githubClient;

    public GithubOauth(GithubProvider githubProvider, GithubClient githubClient) {
        this.githubProvider = githubProvider;
        this.githubClient = githubClient;
    }

    public GithubProfileResponse getProfile(String code) {
        return requestGithubProfile(requestGithubAccessToken(code));
    }

    private GithubAccessToken requestGithubAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        GithubTokenRequest githubTokenRequest = new GithubTokenRequest(
                githubClient.getId(),
                githubClient.getSecret(),
                code
        );
        HttpEntity<GithubTokenRequest> httpEntity = new HttpEntity<>(githubTokenRequest, httpHeaders);
        return restTemplate.exchange(
                githubProvider.getTokenUrl(),
                HttpMethod.POST,
                httpEntity,
                GithubAccessToken.class
        ).getBody();
    }

    private GithubProfileResponse requestGithubProfile(GithubAccessToken accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken.accessToken());

        HttpEntity<Void> httpEntity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                githubProvider.getUserInfoUrl(),
                HttpMethod.GET,
                httpEntity,
                GithubProfileResponse.class
        ).getBody();
    }
}
