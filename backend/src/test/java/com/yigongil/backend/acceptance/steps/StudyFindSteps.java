package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;

import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StudyFindSteps {

    private final SharedContext sharedContext;

    public StudyFindSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @When("{string}가 개설한 스터디를 조회한다.")
    public void 개설한_스터디를_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header("Authorization", token)
                                                        .when()
                                                        .get("/studies/created")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }
}
