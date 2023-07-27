package com.yigongil.backend.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.MemberJoinRequest;
import com.yigongil.backend.request.ProfileUpdateRequest;
import com.yigongil.backend.response.MemberResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class MemberSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public MemberSteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @Given("{string}의 깃허브 아이디로 회원가입을 한다.")
    public void 깃허브_아이디로_회원가입을_한다(String githubId) throws JsonProcessingException {
        MemberJoinRequest request = new MemberJoinRequest(githubId);

        String locationHeader = RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/v1/members")
                .then()
                .log()
                .all()
                .extract()
                .header(HttpHeaders.LOCATION);

        String memberId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
        sharedContext.setParameter(githubId, memberId);
    }

    @When("{string}가 닉네임 {string}과 간단 소개{string}으로 수정한다.")
    public void 닉네임_간단소개_입력(String memberGithubId, String nickname, String introduction) throws JsonProcessingException {
        ProfileUpdateRequest request = new ProfileUpdateRequest(nickname, introduction);

        ExtractableResponse<Response> response = RestAssured.given()
                .log()
                .all()
                .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(memberGithubId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .patch("/v1/members")
                .then()
                .log()
                .all()
                .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}가 변경된 정보 닉네임 {string}과 간단 소개{string}를 확인할 수 있다.")
    public void profile_확인(String githubId, String nickname, String introduction) {
        MemberResponse response = RestAssured.given()
                .when()
                .get("/v1/members/" + sharedContext.getParameter(githubId))
                .then()
                .extract()
                .as(MemberResponse.class);

        assertAll(
                () -> assertThat(response.nickname()).isEqualTo(nickname),
                () -> assertThat(response.introduction()).isEqualTo(introduction)
        );
    }
}
