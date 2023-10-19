package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import com.yigongil.backend.response.MyStudyResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;

public class HomeSteps {

    private final SharedContext sharedContext;

    public HomeSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @When("{string}가 홈화면을 조회한다.")
    public void 현재_회차를_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/home")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}, null, {int}, {int}, true로 스터디가 반환된다.")
    public void 스터디_반환(String studyName, int leftDays, int grassCount) {
        List<UpcomingStudyResponse> responses = sharedContext.getResponse()
                                                             .jsonPath()
                                                             .getList(".", UpcomingStudyResponse.class);

        UpcomingStudyResponse upcomingStudyResponse = responses.get(0);

        assertAll(
            () -> assertThat(upcomingStudyResponse.name()).isEqualTo(studyName),
            () -> assertThat(upcomingStudyResponse.leftDays()).isEqualTo(leftDays),
            () -> assertThat(upcomingStudyResponse.grassCount()).isEqualTo(grassCount),
            () -> assertThat(upcomingStudyResponse.todoContent()).isNull(),
            () -> assertThat(upcomingStudyResponse.isMaster()).isTrue()
        );
    }
}
