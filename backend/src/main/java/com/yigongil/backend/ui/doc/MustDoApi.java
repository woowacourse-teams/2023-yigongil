package com.yigongil.backend.ui.doc;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.MustDoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "머스트두", description = "머스트두 관련 api")
public interface MustDoApi {

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
