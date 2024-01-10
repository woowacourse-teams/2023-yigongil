package com.yigongil.backend.domain.certification;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.CertificationCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증", description = "인증 관련 api")
public interface CertificationApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "404", content = @Content)
        }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "인증 피드 등록")
    ResponseEntity<Void> createCertification(
        @Schema(hidden = true) Member member,
        @Parameter(description = "피드가 등록되는 스터디 studyId", required = true) Long id,
        CertificationCreateRequest request
    );
}
