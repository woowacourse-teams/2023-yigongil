package com.yigongil.backend.ui.doc;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "투두", description = "투두 관련 api")
public interface TodoApi {

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "404")
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "필수 투두 생성")
    ResponseEntity<Void> createNecessaryTodo(
            @Schema(hidden = true) Member member,
            @Parameter(description = "필수 투두를 생성할 라운드 id", required = true) Long roundId,
            TodoCreateRequest request
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
    @Operation(summary = "선택 투두 생성")
    ResponseEntity<Void> createOptionalTodo(
            @Schema(hidden = true) Member member,
            @Parameter(description = "선택 투두를 생성할 라운드 id", required = true) Long roundId,
            TodoCreateRequest request
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
    @Operation(summary = "필수 투두 수정")
    ResponseEntity<Void> updateNecessaryTodo(
            @Schema(hidden = true) Member member,
            @Parameter(description = "필수 투두를 수정할 라운드 id", required = true) Long roundId,
            TodoUpdateRequest request
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
    @Operation(summary = "선택 투두 수정")
    ResponseEntity<Void> updateOptionalTodo(
            @Schema(hidden = true) Member member,
            @Parameter(description = "선택 투두를 수정할 라운드 id", required = true) Long roundId,
            @Parameter(description = "수정할 선택 투두 id", required = true) Long todoId,
            TodoUpdateRequest request
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
    @Operation(summary = "선택 투두 삭제")
    ResponseEntity<Void> deleteTodo(
            @Schema(hidden = true) Member member,
            @Parameter(description = "선택 투두를 삭제할 라운드 id", required = true) Long roundId,
            @Parameter(description = "삭제할 선택 투두 id", required = true) Long todoId
    );
}
