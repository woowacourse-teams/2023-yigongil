package com.yigongil.backend.ui;

import com.yigongil.backend.application.MemberService;
import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.MemberJoinRequest;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.MyProfileResponse;
import com.yigongil.backend.response.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/v1/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable Long id) {
        ProfileResponse response = memberService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/my")
    public ResponseEntity<MyProfileResponse> findMyProfile(@Authorization Member member) {
        ProfileResponse profile = memberService.findById(member.getId());
        MyProfileResponse response = new MyProfileResponse(
                profile.id(),
                profile.nickname(),
                profile.githubId(),
                profile.profileImageUrl(),
                profile.successRate(),
                profile.successfulRoundCount(),
                profile.tierProgress(),
                profile.tier(),
                profile.introduction()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<Void> updateProfile(@Authorization Member member, @RequestBody ProfileUpdateRequest request) {
        memberService.update(member, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberJoinRequest request) {
        Long id = memberService.join(request);
        return ResponseEntity.created(URI.create("/v1/members/" + id)).build();
    }
}
