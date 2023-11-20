package com.yigongil.backend.query.feed;

import com.yigongil.backend.response.FeedPostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

@Tag(name = "피드", description = "피드 관련 api")
public interface FeedQueryApi {

        @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200"),
                @ApiResponse(responseCode = "400", content = @Content),
                @ApiResponse(responseCode = "401", content = @Content),
                @ApiResponse(responseCode = "404", content = @Content)
            }
        )
        @SecurityRequirement(name = "token")
        @Operation(summary = "피드 조회")
        ResponseEntity<List<FeedPostResponse>> findFeedPosts(
            @Parameter(description = "조회할 스터디 id", required = true) Long id,
            @Parameter(description = "마지막으로 본 피드의 아이디, 첫 요청에서는 필요 없음", allowEmptyValue = true) Optional<Long> oldestFeedPostId
        );
}
