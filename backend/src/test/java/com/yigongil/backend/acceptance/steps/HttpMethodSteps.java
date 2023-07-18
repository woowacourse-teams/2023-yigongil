package com.yigongil.backend.acceptance.steps;

import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class HttpMethodSteps {

    private final SharedContext sharedContext;

    public HttpMethodSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @When("patch 요청을 {string}로 보낸다")
    public void patch_요청(String url) {
        RequestSpecification requestSpecification = sharedContext.getRequestSpecification();

        RestAssured.given()
                .spec(requestSpecification)
                .when()
                .patch(url);
    }

    @When("post 요청을 {string} 로 보낸다.")
    public void post_요청(String url) {
        RequestSpecification requestSpecification = sharedContext.getRequestSpecification();

        RestAssured.given()
                .spec(requestSpecification)
                .when()
                .post(url);
    }
}
