package com.yigongil.backend.ui;

import com.yigongil.backend.application.MemberService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.ProfileUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping
    public ResponseEntity<Void> updateProfile(@Authorization Member member, @RequestBody ProfileUpdateRequest request) {
        memberService.updateMember(member, request);
        return ResponseEntity.ok().build();
    }
}
