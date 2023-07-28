package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class TodoSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public TodoSteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @When("{string}가 {string}, {string}, {string}로 이름이 {string}인 스터디에 투두를 추가한다.")
    public void 투두_추가(String studyMemberGithubId, String isNecessary, String roundId, String content, String studyName) throws JsonProcessingException {
        TodoCreateRequest request = new TodoCreateRequest(
                Boolean.parseBoolean(isNecessary),
                Long.parseLong(roundId),
                content
        );

        String locationHeader = given().log().all()
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .body(objectMapper.writeValueAsString(request))
                                       .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(studyMemberGithubId))
                                       .when()
                                       .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/todos")
                                       .then().log().all()
                                       .extract()
                                       .header(HttpHeaders.LOCATION);

        sharedContext.setParameter("todoId", locationHeader.substring(locationHeader.lastIndexOf("/") + 1));
    }

    @When("{string}가 {string} 이름의 스터디에서 {string} 투두의 수정 내용 {string}, {string}으로 수정한다.")
    public void 투두의_수정_내용을_입력한다(String githubId, String studyName, String isNecessary, String isDone, String content) throws JsonProcessingException {
        TodoUpdateRequest request = new TodoUpdateRequest(
                Boolean.parseBoolean(isNecessary),
                Boolean.parseBoolean(isDone),
                content
        );

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(githubId))
                                                        .when()
                                                        .patch("/v1/studies/" + sharedContext.getParameter(studyName) + "/todos/" + sharedContext.getParameter("todoId"))
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @When("{string}가 {string} 이름의 스터디에서 등록한 투두를 삭제한다.")
    public void 투두를_삭제한다(String githubId, String studyName) throws JsonProcessingException {
        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(githubId))
                                                        .when()
                                                        .delete("/v1/studies/" + sharedContext.getParameter(studyName) + "/todos/" + sharedContext.getParameter(
                                                                "todoId"))
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("투두를 확인할 수 있다.")
    public void 투두를확인할수있다() {
        // TODO: 2023/07/19 나중에 구현
    }

    @Then("수정된 내용 {string}, {string} 이 투두에 반영된다.")
    public void 수정된_내용이_투두에_반영된다(String isDone, String content) {
        // TODO: 2023/07/20 회차 정보 조회 api로 리팩토링 하기
    }
}
