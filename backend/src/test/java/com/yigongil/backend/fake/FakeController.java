package com.yigongil.backend.fake;

import com.yigongil.backend.application.StudyService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.config.auth.JwtTokenProvider;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.infra.MessagingService;
import com.yigongil.backend.request.MessagingTokenRequest;
import com.yigongil.backend.response.TokenResponse;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile(value = {"local", "test"})
@RestController
public class FakeController {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final StudyService studyService;
    private final MessagingService messagingService;

    public FakeController(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, StudyService studyService, final MessagingService messagingService) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.studyService = studyService;
        this.messagingService = messagingService;
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

    @PostMapping("/login/fake/messaging/tokens")
    public ResponseEntity<Void> registerDevice(
            @Authorization Member member,
            @RequestBody MessagingTokenRequest request
    ) {
        messagingService.registerToken(request.token(), member);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/fake/proceed")
    public ResponseEntity<Void> proceed(@RequestParam Integer days) {
        for (int i = 1; i <= days; i++) {
            studyService.proceedRound(LocalDate.now().plusDays(i));
        }
        return ResponseEntity.ok().build();
    }
}
