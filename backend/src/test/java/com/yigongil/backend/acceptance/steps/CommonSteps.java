package com.yigongil.backend.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.config.auth.JwtTokenProvider;
import io.cucumber.java.en.Then;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CommonSteps {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public CommonSteps(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper,
        SharedContext sharedContext) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @Then("{int} 응답을 받는다.")
    public void 응답_검사(int expected) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertThat(response.statusCode()).isEqualTo(expected);
    }
}
