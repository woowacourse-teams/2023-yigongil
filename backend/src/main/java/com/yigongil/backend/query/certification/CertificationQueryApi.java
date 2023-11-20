package com.yigongil.backend.query.certification;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.response.CertificationResponse;
import com.yigongil.backend.response.MembersCertificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 조회", description = "인증 조회 관련 api")
public interface CertificationQueryApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content)
        }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 멤버 전체 인증 정보 조회")
    ResponseEntity<MembersCertificationResponse> findAllMembersCertification(
        @Schema(hidden = true) Member member,
        @Parameter(description = "조회하려는 스터디 id", required = true) Long id
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", content = @Content)
        }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 멤버 단일 인증 게시글 조회")
    ResponseEntity<CertificationResponse> findMemberCertification(
        @Parameter(description = "인증 게시 회차 id") Long roundId,
        @Parameter(description = "작성자 id") Long memberId
    );
}
