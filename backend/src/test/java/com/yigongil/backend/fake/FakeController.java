package com.yigongil.backend.fake;

import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.domain.member.domain.MemberRepository;
import com.yigongil.backend.domain.round.RoundService;
import com.yigongil.backend.response.TokenResponse;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile(value = {"local", "test"})
@RestController
public class FakeController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoundService roundService;

    public FakeController(
        MemberRepository memberRepository,
        JwtTokenProvider jwtTokenProvider,
        RoundService roundService
    ) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roundService = roundService;
    }

    @GetMapping("/login/fake/tokens")
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
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(new TokenResponse(jwtTokenProvider.createAccessToken(id), jwtTokenProvider.createRefreshToken(id)));
    }

    @PutMapping("/fake/proceed")
    public ResponseEntity<Void> proceed(@RequestParam Integer days) {
        for (int i = 1; i <= days; i++) {
            roundService.proceedRound(LocalDate.now().plusDays(i));
        }
        return ResponseEntity.ok().build();
    }
}
