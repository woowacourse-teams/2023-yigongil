package com.yigongil.backend.application;

import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.config.oauth.GithubOauth;
import com.yigongil.backend.config.oauth.GithubProfileResponse;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.request.TokenRequest;
import com.yigongil.backend.response.TokenResponse;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final GithubOauth githubOauth;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(GithubOauth githubOauth, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.githubOauth = githubOauth;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(String code) {
        GithubProfileResponse profileResponse = githubOauth.getProfile(code);

        Optional<Member> member = memberRepository.findByGithubId(profileResponse.githubId());

        Long id = member.orElseGet(
                () -> memberRepository.save(
                        Member.builder()
                              .githubId(profileResponse.githubId())
                              .profileImageUrl(profileResponse.profileImageUrl())
                              .build()
                )
        ).getId();

        return createTokens(id);
    }

    public TokenResponse refresh(TokenRequest request) {
        String refreshToken = request.refreshToken();

        jwtTokenProvider.detectTokenTheft(refreshToken);
        Long memberId = jwtTokenProvider.parseToken(refreshToken);

        return createTokens(memberId);
    }

    private TokenResponse createTokens(Long id) {
        return new TokenResponse(jwtTokenProvider.createAccessToken(id), jwtTokenProvider.createRefreshToken(id));
    }
}
