package com.yigongil.backend.application;

import com.yigongil.backend.config.oauth.GithubAccessToken;
import com.yigongil.backend.config.oauth.GithubClient;
import com.yigongil.backend.config.oauth.GithubProfileResponse;
import com.yigongil.backend.config.oauth.GithubProvider;
import com.yigongil.backend.config.oauth.GithubTokenRequest;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import java.net.URI;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthService {

    private final GithubProvider githubProvider;
    private final GithubClient githubClient;
    private final MemberRepository memberRepository;

    public OauthService(GithubProvider githubProvider, GithubClient githubClient, MemberRepository memberRepository) {
        this.githubProvider = githubProvider;
        this.githubClient = githubClient;
        this.memberRepository = memberRepository;
    }

    public Long login(String authCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        GithubTokenRequest githubTokenRequest = new GithubTokenRequest(
                githubClient.getId(),
                githubClient.getSecret(),
                authCode,
                githubClient.getRedirectUrl()
        );
        HttpEntity<GithubTokenRequest> httpEntity = new HttpEntity<>(githubTokenRequest, httpHeaders);
        GithubAccessToken accessToken = restTemplate.exchange(
                URI.create(githubProvider.getTokenUrl()),
                HttpMethod.POST,
                httpEntity,
                GithubAccessToken.class
        ).getBody();

        HttpHeaders httpHeaders2 = new HttpHeaders();
        httpHeaders2.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.accessToken());

        HttpEntity<Void> httpEntity2 = new HttpEntity<>(httpHeaders2);

        GithubProfileResponse profileResponse = restTemplate.exchange(
                githubProvider.getUserInfoUrl(),
                HttpMethod.GET,
                httpEntity2,
                GithubProfileResponse.class
        ).getBody();

        Optional<Member> member = memberRepository.findByGithubId(profileResponse.githubId());

        return member.orElseGet(
                () -> memberRepository.save(Member.builder()
                                                  .githubId(profileResponse.githubId())
                                                  .profileImageUrl(profileResponse.profileImageUrl())
                                                  .build())
        ).getId();
    }
}
