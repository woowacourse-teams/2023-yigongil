package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.TokenRequest;
import com.yigongil.backend.response.TokenResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthorizationSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public AuthorizationSteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @When("{string}가 토큰이 만료되어 리프레시 토큰을 사용해 새 토큰을 받는다.")
    public void 토큰_리프레시(String githubId) throws JsonProcessingException {
        TokenRequest request = new TokenRequest(sharedContext.getRefresh(githubId));

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .when()
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .post("/v1/login/tokens/refresh")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
        if (response.statusCode() < 300) {
            TokenResponse tokenResponse = response.as(TokenResponse.class);
            sharedContext.setTokens(githubId, tokenResponse.accessToken());
            sharedContext.setRefresh(githubId, tokenResponse.refreshToken());
        }
    }

    @Then("{string}가 새로운 액세스 토큰과 새로운 리프레시 토큰을 200응답으로 받았음을 확인할 수 있다.")
    public void 유효한_리프레시_토큰(String githubId) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Given("{string}가 {string}의 리프레시 토큰을 탈취하여 새 토큰을 받는다.")
    public void 토큰_탈취(String thief, String victim) throws JsonProcessingException {
        TokenRequest request = new TokenRequest(sharedContext.getRefresh(victim));

        TokenResponse response = given().log().all()
                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                        .when()
                                        .body(objectMapper.writeValueAsString(request))
                                        .post("/v1/login/tokens/refresh")
                                        .then().log().all()
                                        .extract()
                                        .as(TokenResponse.class);

        sharedContext.setTokens(thief, response.accessToken());
        sharedContext.setRefresh(thief, response.refreshToken());
    }

    @Then("{string}가 인증에 실패한다.")
    public void 인증_실패(String githubId) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
