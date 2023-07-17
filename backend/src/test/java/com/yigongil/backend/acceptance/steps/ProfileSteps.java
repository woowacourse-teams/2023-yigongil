package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.ProfileUpdateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.springframework.http.MediaType;

public class ProfileSteps {

    private final ObjectMapper objectMapper;

    public ProfileSteps(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Given("닉네임 {string}과 간단 소개{string}을 정상적으로 입력한다")
    public void 닉네임_간단소개_입력(String nickname, String introduction) throws JsonProcessingException {
        final ProfileUpdateRequest request = new ProfileUpdateRequest(nickname, introduction);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request));
    }

    @When("{string}로 patch 요청을 보낸다")
    public void patch_요청(String url) {
        RestAssured.when()
                .patch(url);
    }

    @Then("변경된 정보를 확인할 수 있다")
    public void 변경된정보를확인할수있다() {
        System.out.println("sdf");
    }
}
