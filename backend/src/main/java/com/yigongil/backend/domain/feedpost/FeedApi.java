package com.yigongil.backend.domain.feedpost;

import com.yigongil.backend.domain.member.domain.Member;
import com.yigongil.backend.request.FeedPostCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "피드", description = "피드 관련 api")
public interface FeedApi {

    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "401", content = @Content),
            @ApiResponse(responseCode = "404", content = @Content)
        }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "일반 피드 등록")
    ResponseEntity<Void> createFeedPost(
        @Schema(hidden = true) Member member,
        @Parameter(description = "피드가 등록되는 스터디 id", required = true) Long id,
        FeedPostCreateRequest request
    );
}
