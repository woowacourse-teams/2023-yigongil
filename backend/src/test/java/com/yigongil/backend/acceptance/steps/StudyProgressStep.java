package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.response.StudyDetailResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Duration;
import org.springframework.http.HttpHeaders;

public class StudyProgressStep {

    private static final int ONE_DAY = 2;

    private final SharedContext sharedContext;

    public StudyProgressStep(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @Given("1일이 지난다.")
    public void 시간_소요() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(ONE_DAY).toMillis());
    }

    @When("{string}가 {string} 스터디를 조회한다.")
    public void 스터디_조회(String memberGithubId, String studyName) {
        String memberId = (String) sharedContext.getParameter(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        ExtractableResponse<Response> response = given()
                .header(HttpHeaders.AUTHORIZATION, memberId)
                .when()
                .get("/v1/studies/" + studyId)
                .then()
                .log()
                .all()
                .extract();

        sharedContext.setResponse(response);
    }

    @Then("자바1 스터디의 현재 라운드가 변경되어 있다.")
    public void 스터디_현재_회차_조회() {
        int prevRoundNumber = 1;

        StudyDetailResponse studyDetailResponse = sharedContext.getResponse().as(StudyDetailResponse.class);

        assertThat(studyDetailResponse.currentRound()).isEqualTo(prevRoundNumber + 1);
    }
}