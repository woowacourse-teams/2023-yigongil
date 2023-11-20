package com.yigongil.backend.query.profile;

import com.yigongil.backend.config.auth.Authorization;
import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.response.MyProfileResponse;
import com.yigongil.backend.response.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
public class ProfileController implements ProfileApi {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable Long id) {
        ProfileResponse response = profileService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/my")
    public ResponseEntity<MyProfileResponse> findMyProfile(@Authorization Member member) {
        ProfileResponse profile = profileService.findById(member.getId());
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
}
