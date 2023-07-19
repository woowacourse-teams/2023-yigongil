package com.yigongil.backend.acceptance.steps;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class SharedContext {

    private RequestSpecification requestSpecification;
    private ExtractableResponse<Response> response;

    public RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public void setResponse(ExtractableResponse<Response> response) {
        this.response = response;
    }
}
