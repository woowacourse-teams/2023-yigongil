package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.domain.studymember.Role;
import com.yigongil.backend.response.MyStudyResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;

public class MyStudyFindSteps {

    private final SharedContext sharedContext;

    public MyStudyFindSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @When("{string}의 모든 스터디를 조회한다.")
    public void 나의_스터디_조회(String githubId) {
        String memberId = sharedContext.getToken(githubId);

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
                                                   .filter(myStudyResponse -> myStudyResponse.role() == Role.MASTER.getCode())
                                                   .count();

        int actualStudyMemberRoleCount = (int) myStudies.stream()
                                                        .filter(myStudyResponse -> myStudyResponse.role() == Role.STUDY_MEMBER.getCode())
                                                        .count();

        int actualApplicantRoleCount = (int) myStudies.stream()
                                                      .filter(myStudyResponse -> myStudyResponse.role() == Role.APPLICANT.getCode())
                                                      .count();

        int actualNoRoleCount = (int) myStudies.stream()
                                               .filter(myStudyResponse -> myStudyResponse.role() == Role.NO_ROLE.getCode())
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
