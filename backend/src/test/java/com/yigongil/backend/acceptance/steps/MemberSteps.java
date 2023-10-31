package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.NicknameValidationResponse;
import com.yigongil.backend.response.OnboardingCheckResponse;
import com.yigongil.backend.response.ProfileResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.TokenResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public MemberSteps(
            ObjectMapper objectMapper,
            SharedContext sharedContext
    ) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @Given("{string}의 깃허브 아이디로 회원가입을 한다.")
    public void 깃허브_아이디로_회원가입을_한다(String githubId) {
        TokenResponse tokenResponse = given().log().all()
                                             .when()
                                             .get("/login/fake/tokens?githubId=" + githubId)
                                             .then().log().all()
                                             .extract()
                                             .as(TokenResponse.class);

        sharedContext.setTokens(githubId, tokenResponse.accessToken());
        sharedContext.setId(githubId, tokenResponse.accessToken());
        Member member = Member.builder()
                              .id(sharedContext.getId(githubId))
                              .githubId(githubId)
                              .nickname(githubId)
                              .profileImageUrl("this_is_fake_image_url")
                              .isOnboardingDone(true)
                              .build();
        sharedContext.setMember(githubId, member);
        sharedContext.setRefresh(githubId, tokenResponse.refreshToken());
    }

    @When("{string}가 닉네임 {string}과 간단 소개{string}으로 수정한다.")
    public void 닉네임_간단소개_입력(
            String memberGithubId,
            String nickname,
            String introduction
    ) throws JsonProcessingException {
        ProfileUpdateRequest request = new ProfileUpdateRequest(nickname, introduction);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(memberGithubId))
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .when()
                                                        .patch("/members")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}가 변경된 정보 닉네임 {string}과 간단 소개{string}를 확인할 수 있다.")
    public void profile_확인(String githubId, String nickname, String introduction) {
        ProfileResponse response = given()
                .when()
                .get("/members/" + sharedContext.getParameter(githubId))
                .then()
                .extract()
                .as(ProfileResponse.class);

        assertAll(
                () -> assertThat(response.nickname()).isEqualTo(nickname),
                () -> assertThat(response.introduction()).isEqualTo(introduction)
        );
    }

    @When("{string}가 회원 탈퇴한다.")
    public void 회원이_탈퇴한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .delete("/members")
               .then().log().all();
    }

    @When("{string}가 마이페이지를 조회한다.")
    public void 마이페이지를_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/members/my")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}은 중복된 닉네임인 것을 확인할 수 있다.")
    public void 중복_닉네임_확인(String nickname) {
        ExtractableResponse<Response> response = given()
                .when()
                .get("/members/exists?nickname=" + nickname)
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(NicknameValidationResponse.class).exists()).isTrue()
        );
    }

    @Then("{string}의 온보딩 상태가 완료로 변경된다.")
    public void 온보딩_상태_검증(String githubId) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(githubId))
                                                        .when()
                                                        .get("members/check-onboarding-is-done")
                                                        .then().log().all()
                                                        .statusCode(HttpStatus.OK.value())
                                                        .extract();

        assertThat(response.as(OnboardingCheckResponse.class).isOnboardingDone()).isTrue();
    }

    @Then("{string}가 회원 탈퇴 상태이다.")
    public void 회원_탈퇴한_상태이다(String githubId) {
        Long id = sharedContext.getId(githubId);
        StudyDetailResponse response = sharedContext.getResponse().as(StudyDetailResponse.class);

        Boolean deleted = response.members().stream()
                                  .filter(it -> it.id().equals(id))
                                  .findFirst()
                                  .get().isDeleted();

        assertThat(deleted).isTrue();
    }

    @When("{string}이 {string}의 프로필을 조회한다.")
    public void 프로필을_조회한다(String githubId1, String githubId2) {
        Long id = sharedContext.getId(githubId2);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(githubId1))
                                                        .when()
                                                        .get("/members/" + id)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("조회한 멤버의 경험치가 상승했다.")
    public void 멤버의_티어를_검증한다() {
        ProfileResponse response = sharedContext.getResponse().as(ProfileResponse.class);

        assertThat(response.tierProgress()).isGreaterThan(0);
    }
}
