package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.ProfileUpdateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class ProfileSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public ProfileSteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @Given("닉네임 {string}과 간단 소개{string}을 정상적으로 입력한다")
    public void 닉네임_간단소개_입력(String nickname, String introduction) throws JsonProcessingException {
        ProfileUpdateRequest request = new ProfileUpdateRequest(nickname, introduction);

        final RequestSpecification requestSpecification = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request));

        sharedContext.setRequestSpecification(requestSpecification);
    }

    @Then("변경된 정보를 확인할 수 있다")
    public void 변경된_정보를_확인_할_수_있다() {
        // TODO: 2023/07/18 조회Api생성후리팩터링
    }
}
