package com.yigongil.backend.acceptance.steps;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ScenarioScope
public class SharedContext {

    private ExtractableResponse<Response> response;
    private final Map<String, Object> parameters = new HashMap<>();

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void setResponse(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }
}
