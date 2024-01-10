package com.yigongil.backend.domain.member.ui;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.application.MemberService;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.NicknameValidationResponse;
import com.yigongil.backend.response.OnboardingCheckResponse;
import com.yigongil.backend.ui.doc.MemberApi;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class MemberController implements MemberApi {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping
    public ResponseEntity<Void> updateProfile(
        @Authorization Member member,
        @RequestBody @Valid ProfileUpdateRequest request
    ) {
        memberService.update(member, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Authorization Member member) {
        memberService.delete(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/exists")
    public ResponseEntity<NicknameValidationResponse> existsByNickname(@RequestParam String nickname) {
        NicknameValidationResponse response = memberService.existsByNickname(nickname);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/check-onboarding-is-done")
    public ResponseEntity<OnboardingCheckResponse> checkOnboardingIsDone(@Authorization Member member) {
        OnboardingCheckResponse response = new OnboardingCheckResponse(member.isOnboardingDone());
        return ResponseEntity.ok(response);
    }
}
