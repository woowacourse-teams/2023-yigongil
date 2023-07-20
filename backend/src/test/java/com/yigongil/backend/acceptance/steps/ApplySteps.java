package com.yigongil.backend.acceptance.steps;

import com.yigongil.backend.response.StudyMemberResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplySteps {

    private final SharedContext sharedContext;

    public ApplySteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @Given("깃허브 아이디가 {string}인 멤버가 이름이 {string}스터디에 신청할 수 있다.")
    public void 스터디_신청(String githubId, String studyName) {
        String memberId = (String) sharedContext.getParameter(githubId);
        given().log()
               .all()
               .header(HttpHeaders.AUTHORIZATION, memberId)
               .when()
               .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/applicants")
               .then()
               .log()
               .all();
    }

    @When("{string}가 이름이 {string}인 스터디의 신청자를 조회한다.")
    public void 스터디_신청자_조회(String masterGithubId, String studyName) {
        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(masterGithubId))
                                                        .when()
                                                        .get("/v1/studies/" + sharedContext.getParameter(studyName) + "/applicants")
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("스터디의 신청자는 {int}명이다.")
    public void 신청자_확인(int applicantsCount) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<StudyMemberResponse> studyMemberResponses = response.jsonPath()
                                                                 .getList(".", StudyMemberResponse.class);

        assertThat(studyMemberResponses).hasSize(applicantsCount);
    }
}
