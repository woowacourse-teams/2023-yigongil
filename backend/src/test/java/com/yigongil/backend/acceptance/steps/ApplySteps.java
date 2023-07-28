package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;

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
        ExtractableResponse<Response> response =
                given().log()
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

    @When("{string}가 {string}의 {string} 스터디 신청을 수락한다.")
    public void 스터디_신청_수락(String masterName, String memberName, String studyName) {
        Object studyId = sharedContext.getParameter(studyName);
        Object memberId = sharedContext.getParameter(memberName);

        ExtractableResponse<Response> response =
                given().log().all()
                       .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(masterName))
                       .when()
                       .patch("/v1/studies/{studyId}/applicants/{memberId}", studyId, memberId)
                       .then()
                       .log().all()
                       .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}는 {string} 스터디의 스터디원으로 추가되어 있다.")
    public void 스터디원_추가_완료(String memberName, String studyName) {
        String memberId = String.valueOf(sharedContext.getParameter(memberName));

        StudyDetailResponse response = sharedContext.getResponse()
                                                    .as(StudyDetailResponse.class);
        List<StudyMemberResponse> studyMembers = response.members();

        assertThat(studyMembers).anyMatch(member -> member.id().equals(Long.valueOf(memberId)));
    }


    @When("{string}이 {string} 스터디 신청을 취소한다.")
    public void 스터디_신청_취소(String applicantName, String studyName) {
        ExtractableResponse<Response> response =
                given().log().all()
                       .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(applicantName))
                       .when()
                       .delete("/v1/studies/{studyId}/applicants", sharedContext.getParameter(studyName))
                       .then().log().all()
                       .extract();

        sharedContext.setResponse(response);
    }
}
