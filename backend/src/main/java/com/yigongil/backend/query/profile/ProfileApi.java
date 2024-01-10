package com.yigongil.backend.query.profile;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.response.MyProfileResponse;
import com.yigongil.backend.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "프로필 조회", description = "프로필 조회 api")
public interface ProfileApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = @Content)
        }
    )
    @Operation(summary = "프로필을 조회")
    ResponseEntity<ProfileResponse> findProfile(
        @Parameter(description = "조회할 회원의 식별자", required = true) Long id
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content)
        }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "내 프로필을 조회")
    ResponseEntity<MyProfileResponse> findMyProfile(@Schema(hidden = true) Member member);
}
