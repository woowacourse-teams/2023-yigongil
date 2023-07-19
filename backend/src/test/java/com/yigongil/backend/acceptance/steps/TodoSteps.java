package com.yigongil.backend.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.optionaltodo.OptionalTodo;
import com.yigongil.backend.domain.optionaltodo.OptionalTodoRepository;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;

public class TodoSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;
    private final OptionalTodoRepository optionalTodoRepository;

    public TodoSteps(ObjectMapper objectMapper, SharedContext sharedContext,
                     OptionalTodoRepository optionalTodoRepository) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
        this.optionalTodoRepository = optionalTodoRepository;
    }

    @When("{string}가 {string}, {string}, {string}로 이름이 {string}인 스터디에 투두를 추가한다.")
    public void 투두_추가(String studyMemberGithubId, String isNecessary, String roundId, String content, String studyName) throws JsonProcessingException {
        TodoCreateRequest request = new TodoCreateRequest(
                Boolean.parseBoolean(isNecessary),
                Long.parseLong(roundId),
                content
        );

        ExtractableResponse<Response> response = RestAssured.given()
                                                            .log()
                                                            .all()
                                                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                            .body(objectMapper.writeValueAsString(request))
                                                            .header(HttpHeaders.AUTHORIZATION, sharedContext.getParameter(studyMemberGithubId))
                                                            .when()
                                                            .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/todos")
                                                            .then()
                                                            .log()
                                                            .all()
                                                            .extract();

        sharedContext.setResponse(response);
    }

    @Then("투두를 확인할 수 있다.")
    public void 투두를확인할수있다() {
        // TODO: 2023/07/19 나중에 구현
    }

    @Given("{string} 투두의 수정 내용 {string}, {string} 을 입력한다.")
    public void 투두의_수정_내용을_입력한다(String isNecessary, String isDone, String content) throws JsonProcessingException {
        TodoUpdateRequest request = new TodoUpdateRequest(
                Boolean.parseBoolean(isNecessary),
                Boolean.parseBoolean(isDone),
                content
        );

        final RequestSpecification specification = RestAssured.given()
                                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                              .body(objectMapper.writeValueAsString(request));

        sharedContext.setRequestSpecification(specification);
    }

    @Then("수정된 내용 {string}, {string} 이 투두에 반영된다.")
    public void 수정된_내용이_투두에_반영된다(String isDone, String content) {
        Optional<OptionalTodo> optionalTodo = optionalTodoRepository.findById(sharedContext.getResultId());
        assertAll(
                () -> assertThat(optionalTodo.get().isDone()).isEqualTo(Boolean.parseBoolean(isDone)),
                () -> assertThat(optionalTodo.get().getContent()).isEqualTo(content)
        );
    }
}
