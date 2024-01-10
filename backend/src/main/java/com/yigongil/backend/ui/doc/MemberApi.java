package com.yigongil.backend.ui.doc;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.NicknameValidationResponse;
import com.yigongil.backend.response.OnboardingCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원", description = "회원 관련 api")
public interface MemberApi {

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "내 회원 정보를 수정")
    ResponseEntity<Void> updateProfile(
            @Schema(hidden = true) Member member,
            ProfileUpdateRequest request
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "회원 탈퇴")
    ResponseEntity<Void> deleteMember(@Schema(hidden = true) Member member);

    @ApiResponse(responseCode = "200")
    @Operation(summary = "닉네임 중복 검사")
    ResponseEntity<NicknameValidationResponse> existsByNickname(
            @Parameter(description = "중복 검사할 닉네임", required = true) String nickname
    );

    @ApiResponse(responseCode = "200")
    @SecurityRequirement(name = "token")
    @Operation(summary = "온보딩 여부 검사")
    ResponseEntity<OnboardingCheckResponse> checkOnboardingIsDone(@Schema(hidden = true) Member member);

}
