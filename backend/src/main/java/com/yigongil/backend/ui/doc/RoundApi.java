package com.yigongil.backend.ui.doc;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.response.RoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회차", description = "회차 관련 api")
public interface RoundApi {

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "회차 상세 조회")
    public ResponseEntity<RoundResponse> viewRoundDetail(
            @Schema(hidden = true) Member member,
            @Parameter(description = "스터디 id", required = true) Long studyId,
            @Parameter(description = "회차 id", required = true) Long roundId
    );
}
