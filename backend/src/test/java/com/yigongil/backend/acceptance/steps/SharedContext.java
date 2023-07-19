package com.yigongil.backend.acceptance.steps;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ScenarioScope
public class SharedContext {

    private RequestSpecification requestSpecification;
    private Long resultId;
    private Long roundId;
    private ExtractableResponse<Response> response;
    private String token;
    private Map<String, Object> parameters = new HashMap<>();

    public RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public Long getResultId() {
        return resultId;
    }

    public Long getRoundId() {
        return roundId;
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public String getToken() {
        return token;
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public void setResponse(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
