package com.yigongil.backend.ui.doc;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.response.UpcomingStudyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "홈 화면", description = "홈 화면 api")
public interface HomeApi {

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "홈 화면 조회")
    ResponseEntity<List<UpcomingStudyResponse>> home(@Schema(hidden = true) Member member);
}
