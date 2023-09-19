package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.TodoCreateRequest;
import com.yigongil.backend.request.TodoUpdateRequest;
import com.yigongil.backend.response.ProgressRateResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.TodoResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TodoSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public TodoSteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @Given("{string}가 찾은 회차에 {string}로 필수 투두를 추가한다.")
    public void 투두_추가(String studyMemberGithubId, String content) throws JsonProcessingException {
        TodoCreateRequest request = new TodoCreateRequest(content);

        given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(objectMapper.writeValueAsString(request))
               .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(studyMemberGithubId))
               .when()
               .post("/v1/rounds/" + sharedContext.getParameter("roundId") + "/todos/necessary")
               .then().log().all();
    }

    @Given("{string}가 찾은 회차에 {string}로 선택 투두를 추가한다.")
    public void 선택_투두_추가(String studyMemberGithubId, String content) throws JsonProcessingException {
        TodoCreateRequest request = new TodoCreateRequest(content);

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(studyMemberGithubId))
                                                        .when()
                                                        .post("/v1/rounds/" + sharedContext.getParameter("roundId") + "/todos/optional")
                                                        .then().log().all()
                                                        .extract();

        String[] split = response.header(HttpHeaders.LOCATION).split("/");
        sharedContext.setParameter("todoId", split[split.length - 1]);
    }

    @When("{string}가 찾은 회차의 필수 투두를 수정 내용 {string}, {string}으로 수정한다.")
    public void 필수투두의_수정_내용을_입력한다(String githubId, String isDone, String content) throws JsonProcessingException {
        TodoUpdateRequest request = new TodoUpdateRequest(
                Boolean.parseBoolean(isDone),
                content
        );

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(githubId))
                                                        .when()
                                                        .patch("/v1/rounds/" + sharedContext.getParameter("roundId") + "/todos/necessary")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @When("{string}가 찾은 회차의 선택 투두를 수정 내용 {string}, {string}으로 수정한다.")
    public void 선택투두의_수정_내용을_입력한다(String githubId, String isDone, String content) throws JsonProcessingException {
        TodoUpdateRequest request = new TodoUpdateRequest(
                Boolean.parseBoolean(isDone),
                content
        );

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(githubId))
                                                        .when()
                                                        .patch("/v1/rounds/"
                                                                + sharedContext.getParameter("roundId")
                                                                + "/todos/optional/"
                                                                + sharedContext.getParameter("todoId"))
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @When("{string}가 찾은 회차에서 등록한 선택 투두를 삭제한다.")
    public void 투두를_삭제한다(String githubId) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(githubId))
                                                        .when()
                                                        .delete("/v1/rounds/" +
                                                                sharedContext.getParameter("roundId") +
                                                                "/todos/optional/" +
                                                                sharedContext.getParameter("todoId"))
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("필수 투두가 {string}임을 확인할 수 있다.")
    public void 필수_투두를_확인할_수_있다(String content) {
        RoundResponse response = sharedContext.getResponse().as(RoundResponse.class);

        assertThat(response.necessaryTodo().content()).isEqualTo(content);
    }

    @Then("선택 투두가 {string}임을 확인할 수 있다.")
    public void 선택_투두를_확인할_수_있다(String content) {
        RoundResponse response = sharedContext.getResponse().as(RoundResponse.class);

        assertThat(response.optionalTodos()).map(TodoResponse::content).contains(content);
    }

    @Then("수정된 내용 {string}, {string} 이 필수 투두에 반영된다.")
    public void 수정된_내용이_투두에_반영된다(String isDone, String content) {
        RoundResponse response = sharedContext.getResponse().as(RoundResponse.class);

        assertAll(
                () -> assertThat(response.necessaryTodo().isDone()).isEqualTo(Boolean.valueOf(isDone)),
                () -> assertThat(response.necessaryTodo().content()).isEqualTo(content)
        );
    }

    @Then("수정된 내용 {string}, {string} 이 선택 투두에 반영된다.")
    public void 수정된_내용이_선택_투두에_반영된다(String isDone, String content) {
        RoundResponse response = sharedContext.getResponse().as(RoundResponse.class);

        assertAll(
                () -> assertThat(response.optionalTodos())
                        .map(TodoResponse::isDone)
                        .containsExactly(Boolean.valueOf(isDone)),
                () -> assertThat(response.optionalTodos())
                        .map(TodoResponse::content)
                        .containsExactly(content)
        );
    }

    @Then("투두를 수정할 수 없다.")
    public void 투두를_수정할_수_없다() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Then("투두가 삭제된다.")
    public void 투두가_삭제된다() {
        RoundResponse response = sharedContext.getResponse().as(RoundResponse.class);

        assertThat(response.optionalTodos()).isEmpty();
    }

    @Then("권한 예외가 발생한다.")
    public void 권한_예외_발생() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @When("이름이 {string}인 스터디의 찾은 회차 투두 진행률을 조회한다.")
    public void 회차의_투두_진행률을_조회한다(String studyName) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .when()
                                                        .get(
                                                                "/v1/studies/" + sharedContext.getParameter(studyName)
                                                                        + "/rounds/" + sharedContext.getParameter("roundId")
                                                                        + "/progress-rate"
                                                        )
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("진행률이 {string}이다.")
    public void 기대_진행률_검증(String progressRate) {
        ProgressRateResponse response = sharedContext.getResponse()
                                                     .as(ProgressRateResponse.class);

        assertThat(response.progressRate()).isEqualTo(Integer.parseInt(progressRate));
    }

    @When("{string}가 찾은 회차의 필수 투두를 체크한다.")
    public void 필수_투두를_체크한다(String githubId) throws JsonProcessingException {
        TodoUpdateRequest request = new TodoUpdateRequest(true, null);

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .body(objectMapper.writeValueAsString(request))
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(githubId))
                                                        .when()
                                                        .patch("/v1/rounds/" + sharedContext.getParameter("roundId") + "/todos/necessary")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }
}
