package com.yigongil.backend.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ErrorSteps {

    private final SharedContext sharedContext;

    public ErrorSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @Then("{int} 코드를 반환한다.")
    public void 코드를_반환한다(int code) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertThat(response.statusCode()).isEqualTo(code);
    }
}
