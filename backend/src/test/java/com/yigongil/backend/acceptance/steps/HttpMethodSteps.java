package com.yigongil.backend.acceptance.steps;

import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

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

        final String location = RestAssured.given()
                .spec(requestSpecification)
                .when().log().all()
                .post(url)
                .then()
                .extract()
                .header(HttpHeaders.LOCATION);

        if (Objects.isNull(location)) {
            return;
        }
        sharedContext.setResultId(Long.parseLong(location.substring(location.lastIndexOf("/") + 1)));
    }
}
