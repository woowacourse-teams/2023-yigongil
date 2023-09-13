package com.yigongil.backend.ui.doc;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.CertificationFeedPostCreateRequest;
import com.yigongil.backend.request.RegularFeedPostCreateRequest;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.response.FeedPostsResponse;
import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "스터디", description = "스터디 관련 api")
public interface StudyApi {

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", headers = @Header(name = "Location", description = "생성된 스터디 url")),
                    @ApiResponse(responseCode = "401")
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 생성")
    ResponseEntity<Void> createStudy(
            @Schema(hidden = true) Member member,
            StudyUpdateRequest request
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
    @Operation(summary = "스터디 정보 수정")
    ResponseEntity<Void> updateStudy(
            @Schema(hidden = true) Member member,
            @Parameter(description = "수정할 스터디 id", required = true) Long studyId,
            StudyUpdateRequest request
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "404")
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 지원")
    ResponseEntity<Void> applyStudy(
            @Schema(hidden = true) Member member,
            @Parameter(description = "지원할 스터디 id", required = true) Long studyId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "404")
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 지원 수락")
    ResponseEntity<Void> permitApplicant(
            @Schema(hidden = true) Member master,
            @Parameter(description = "수락할 스터디 id", required = true) Long studyId,
            @Parameter(description = "지원한 회원 id", required = true) Long memberId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "404"),
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 지원 취소")
    ResponseEntity<Void> deleteApplicant(
            @Schema(hidden = true) Member member,
            @Parameter(description = "지원 취소할 스터디 id", required = true) Long studyId
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content),
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 상세 조회")
    ResponseEntity<StudyDetailResponse> viewStudyDetail(
            @Schema(hidden = true) Member member,
            @Parameter(description = "조회할 스터디 id", required = true) Long id
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "모집중인 스터디 조회")
    ResponseEntity<List<RecruitingStudyResponse>> findRecruitingStudies(
            @Parameter(description = "페이지", required = true) int page
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "모집중인 스터디 검색")
    ResponseEntity<List<RecruitingStudyResponse>> findRecruitingStudiesWithSearch(
            @Parameter(description = "페이지", required = true) int page,
            @Parameter(name = "q", description = "검색", required = true) String word
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 지원자 조회")
    ResponseEntity<List<StudyMemberResponse>> findApplicantOfStudy(
            @Parameter(description = "조회할 스터디 id", required = true) Long id,
            @Schema(hidden = true) Member master
    );

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "401", content = @Content),
                    @ApiResponse(responseCode = "404", content = @Content)
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "내 스터디 목록 조회")
    ResponseEntity<List<MyStudyResponse>> findMyStudies(@Schema(hidden = true) Member member);

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "400"),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "404")
            }
    )
    @SecurityRequirement(name = "token")
    @Operation(summary = "스터디 시작")
    ResponseEntity<Void> startStudy(
            @Schema(hidden = true) Member member,
            @Parameter(description = "시작할 스터디 id", required = true) Long id
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
    @Operation(summary = "피드 조회")
    ResponseEntity<FeedPostsResponse> findFeedPosts(
            @Parameter(description = "조회할 스터디 id", required = true) Long id,
            @Parameter(description = "페이지", required = true) int page
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
    @Operation(summary = "일반 피드 등록")
    ResponseEntity<Void> createRegularFeedPost(
            @Schema(hidden = true) Member member,
            @Parameter(description = "피드가 등록되는 스터디 id", required = true) Long id,
            RegularFeedPostCreateRequest request
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
    @Operation(summary = "인증 피드 등록")
    ResponseEntity<Void> createCertificationFeedPost(
            @Schema(hidden = true) Member member,
            @Parameter(description = "피드가 등록되는 스터디 id", required = true) Long id,
            CertificationFeedPostCreateRequest request
    );
}
