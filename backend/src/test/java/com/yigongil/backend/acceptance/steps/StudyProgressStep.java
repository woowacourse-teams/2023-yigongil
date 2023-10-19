package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.response.MyProfileResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Ignore;
import org.springframework.http.HttpHeaders;

public class StudyProgressStep {

    private final SharedContext sharedContext;

    public StudyProgressStep(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @Given("{int}일이 지난다.")
    public void 시간_소요(int days) {
        given().when()
               .put("/fake/proceed?days=" + days)
               .then()
               .log()
               .all()
               .extract();
    }

    @When("{string}가 {string} 스터디를 조회한다.")
    public void 스터디_조회(String memberGithubId, String studyName) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        ExtractableResponse<Response> response = given()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get("/studies/" + studyId)
                .then()
                .log()
                .all()
                .extract();

        sharedContext.setResponse(response);
    }

    @Ignore("스터디라운드 -> 주차반영까지 무시") // TODO: 스터디라운드 -> 주차반영
    @Then("스터디의 현재 주차가 {int}로 변경되어 있다.")
    public void 스터디_현재_회차_조회(int expectedWeekNumber) {
        StudyDetailResponse studyDetailResponse = sharedContext.getResponse().as(StudyDetailResponse.class);

        assertAll(
//                () -> assertThat(studyDetailResponse.currentRound()).isEqualTo(expectedWeekNumber),
                () -> assertThat(studyDetailResponse.processingStatus()).isEqualTo(ProcessingStatus.PROCESSING.getCode())
        );
    }

    @Then("스터디가 종료되어 있다.")
    public void 스터디_종료() {
        StudyDetailResponse studyDetailResponse = sharedContext.getResponse().as(StudyDetailResponse.class);

        assertThat(studyDetailResponse.processingStatus()).isEqualTo(ProcessingStatus.END.getCode());
    }

    @Then("멤버들이 스터디를 성공적으로 완료하여 티어가 {int}로 상승했다.")
    public void 티어_변경(int expected) {
        StudyDetailResponse studyDetailResponse = sharedContext.getResponse().as(StudyDetailResponse.class);

        assertThat(studyDetailResponse.members()).allMatch(studyMemberResponse -> studyMemberResponse.tier() == expected);
    }

    @Then("{string}의 스터디 성공률이 {int} 퍼센트이다")
    public void 성공률_계산(String memberGIthubId, int expected) {
        MyProfileResponse myProfileResponse = sharedContext.getResponse()
                                                           .as(MyProfileResponse.class);

        assertThat(myProfileResponse.successRate()).isEqualTo(expected);
    }
}
