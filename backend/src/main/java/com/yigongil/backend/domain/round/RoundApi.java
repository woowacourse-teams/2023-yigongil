package com.yigongil.backend.domain.round;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.response.RoundResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "라운드", description = "라운드 관련 api")
public interface RoundApi {

    @SecurityRequirement(name = "token")
    @Operation(summary = "주별 회차 정보 조회")
    ResponseEntity<List<RoundResponse>> findRoundDetailsOfWeek(
        @Parameter(description = "조회할 스터디 id", required = true) Long studyId,
        @Parameter(description = "조회할 주차", required = true) Integer weekNumber
    );

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "401"),
            @ApiResponse(responseCode = "404")
        }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = " 머스트두 생성")
    ResponseEntity<Void> updateMustDo(
        @Schema(hidden = true) Member member,
        @Parameter(description = " 머스트두를 생성할 라운드 id", required = true) Long roundId,
        MustDoUpdateRequest request
    );
}
