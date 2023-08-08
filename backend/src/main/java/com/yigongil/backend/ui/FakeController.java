package com.yigongil.backend.ui;

import com.yigongil.backend.config.oauth.JwtTokenProvider;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.response.TokenResponse;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile(value = {"local", "test"})
@RestController
public class FakeController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public FakeController(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/v1/login/fake/tokens")
    public ResponseEntity<TokenResponse> createFakeToken(@RequestParam String githubId) {
        Optional<Member> member = memberRepository.findByGithubId(githubId);

        Long id = member.orElseGet(
                () -> memberRepository.save(
                        Member.builder()
                              .githubId(githubId)
                              .profileImageUrl("this_is_fake_image_url")
                              .build()
                )
        ).getId();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new TokenResponse(jwtTokenProvider.createToken(id)));
    }
}
