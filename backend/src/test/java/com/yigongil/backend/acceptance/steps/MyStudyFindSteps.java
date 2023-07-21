package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.response.MyStudyResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MyStudyFindSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public MyStudyFindSteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @When("{string}의 모든 스터디를 조회한다.")
    public void 나의_스터디_조회(String githubId) {
        Object memberId = sharedContext.getParameter(githubId);

        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, memberId)
                                                        .when()
                                                        .get("/v1/studies/my")
                                                        .then().log().all()
                                                        .extract();
        sharedContext.setResponse(response);
    }

    @Then("역할이 개설자인 스터디 {int}개 참여자인 스터디 {int}개 지원자인 스터디 {int}개가 표시된다.")
    public void 내_스터디_역할_검증(int masterRoleCount, int studyMemberRoleCount, int applicantRoleCount) {
        List<MyStudyResponse> myStudies = sharedContext.getResponse()
                                                       .jsonPath()
                                                       .getList(".", MyStudyResponse.class);

        int actualMasterRoleCount = (int) myStudies.stream()
                                                   .filter(myStudyResponse -> myStudyResponse.role() == 0)
                                                   .count();

        int actualStudyMemberRoleCount = (int) myStudies.stream()
                                                        .filter(myStudyResponse -> myStudyResponse.role() == 1)
                                                        .count();

        int actualApplicantRoleCount = (int) myStudies.stream()
                                                      .filter(myStudyResponse -> myStudyResponse.role() == 2)
                                                      .count();

        int actualNoRoleCount = (int) myStudies.stream()
                                               .filter(myStudyResponse -> myStudyResponse.role() == 2)
                                               .count();

        assertAll(
                () -> assertThat(actualMasterRoleCount).isEqualTo(masterRoleCount),
                () -> assertThat(actualStudyMemberRoleCount).isEqualTo(studyMemberRoleCount),
                () -> assertThat(actualApplicantRoleCount).isEqualTo(applicantRoleCount),
                () -> assertThat(actualNoRoleCount).isEqualTo(0)
        );
    }

    @Then("모집 중인 스터디 {int}개 진행 중인 스터디 {int}개 종료된 스터디 {int}개가 표시된다.")
    public void 스터디_상태_검증(int recruitingStudyCount, int processingStudyCount, int endedStudyCount) {
        List<MyStudyResponse> myStudies = sharedContext.getResponse()
                                                       .jsonPath()
                                                       .getList(".", MyStudyResponse.class);

        int actualRecruitingStudyCount = (int) myStudies.stream()
                                                        .filter(myStudyResponse -> myStudyResponse.processingStatus() == 0)
                                                        .count();

        int actualProcessingStudyCount = (int) myStudies.stream()
                                                        .filter(myStudyResponse -> myStudyResponse.processingStatus() == 1)
                                                        .count();

        int actualEndedStudyCount = (int) myStudies.stream()
                                                   .filter(myStudyResponse -> myStudyResponse.processingStatus() == 2)
                                                   .count();

        assertAll(
                () -> assertThat(actualRecruitingStudyCount).isEqualTo(recruitingStudyCount),
                () -> assertThat(actualProcessingStudyCount).isEqualTo(processingStudyCount),
                () -> assertThat(actualEndedStudyCount).isEqualTo(endedStudyCount)
        );
    }
}
