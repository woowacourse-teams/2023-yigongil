package com.yigongil.backend.acceptance.steps;

import io.cucumber.spring.ScenarioScope;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class SharedContext {

    private RequestSpecification requestSpecification;
    private Long resultId;

    public RequestSpecification getRequestSpecification() {
        return requestSpecification;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setRequestSpecification(RequestSpecification requestSpecification) {
        this.requestSpecification = requestSpecification;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }
}
