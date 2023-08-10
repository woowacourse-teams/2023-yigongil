package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.config.oauth.JwtTokenProvider;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.NicknameValidationResponse;
import com.yigongil.backend.response.ProfileResponse;
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
    private final JwtTokenProvider jwtTokenProvider;

    public MemberSteps(ObjectMapper objectMapper, SharedContext sharedContext, JwtTokenProvider jwtTokenProvider) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Given("{string}의 깃허브 아이디로 회원가입을 한다.")
    public void 깃허브_아이디로_회원가입을_한다(String githubId) {
        TokenResponse tokenResponse = given().log().all()
                                             .when()
                                             .get("/v1/login/fake/tokens?githubId=" + githubId)
                                             .then().log().all()
                                             .extract()
                                             .as(TokenResponse.class);

        String accessToken = tokenResponse.accessToken();
        sharedContext.setParameter(githubId, accessToken);
    }

    @When("{string}가 닉네임 {string}과 간단 소개{string}으로 수정한다.")
    public void 닉네임_간단소개_입력(String memberGithubId, String nickname, String introduction) throws JsonProcessingException {
        ProfileUpdateRequest request = new ProfileUpdateRequest(nickname, introduction);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(memberGithubId))
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .when()
                                                        .patch("/v1/members")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}가 변경된 정보 닉네임 {string}과 간단 소개{string}를 확인할 수 있다.")
    public void profile_확인(String githubId, String nickname, String introduction) {
        ProfileResponse response = given()
                .when()
                .get("/v1/members/" + jwtTokenProvider.parseToken((String) sharedContext.getParameter(githubId)))
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
        Object token = sharedContext.getParameter(githubId);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .delete("/v1/members")
               .then().log().all();
    }

    @When("{string}가 마이페이지를 조회한다.")
    public void 마이페이지를_조회한다(String githubId) {
        Object token = sharedContext.getParameter(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/members/my")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}은 중복된 닉네임인 것을 확인할 수 있다.")
    public void 중복_닉네임_확인(String nickname) {
        ExtractableResponse<Response> response = given()
                .when()
                .get("/v1/members/exists?nickname=" + nickname)
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(NicknameValidationResponse.class).exists()).isTrue()
        );
    }
}
