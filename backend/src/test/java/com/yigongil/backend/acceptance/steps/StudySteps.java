package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.lang.Long.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StudySteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;

    public StudySteps(ObjectMapper objectMapper, SharedContext sharedContext) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
    }

    @Given("{string}, {string}, {string}, {string}, {string}, {string}를 입력한다.")
    public void 스터디_정보를_입력한다(
            String name,
            String numberOfMaximumMembers,
            String startAt,
            String totalRoundCount,
            String periodOfRound,
            String introduction
    ) throws JsonProcessingException {
        StudyCreateRequest request = new StudyCreateRequest(
                name,
                Integer.parseInt(numberOfMaximumMembers),
                startAt,
                Integer.parseInt(totalRoundCount),
                periodOfRound,
                introduction
        );

        String token = sharedContext.getToken();

        RequestSpecification requestSpecification = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request));

        sharedContext.setRequestSpecification(requestSpecification);
    }

    @Then("스터디 목록에서 해당 스터디를 확인할 수 있다.")
    public void 스터디_목록에서_해당_스터디를_확인할_수_있다() {
        // TODO: 2023/07/18 스터디 조회 api 작성 후 수정
    }

    @When("모집 중인 스터디 {string} 페이지를 요청한다.")
    public void 모집_중인_스터디를_요청한다(String page) {
        ExtractableResponse<Response> response = when().get("/v1/studies/recruiting?page=" + page)
                .then()
                .log()
                .all()
                .extract();

        sharedContext.setResponse(response);
    }

    @Then("모집 중인 스터디를 확인할 수 있다.")
    public void 모집_중인_스터디를_확인할_수_있다() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<RecruitingStudyResponse> recruitingStudyResponses = response.jsonPath()
                .getList(".", RecruitingStudyResponse.class);
        Predicate<RecruitingStudyResponse> isRecruitingPredicate = recruitingStudyResponse -> recruitingStudyResponse.processingStatus() == ProcessingStatus.RECRUITING.getCode();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(recruitingStudyResponses).allMatch(isRecruitingPredicate)
        );
    }

    @Then("스터디 상세 조회에서 해당 스터디를 확인할 수 있다.")
    public void 스터디상세_조회에서_해당_스터디를_확인할_수_있다() {
        Long id = sharedContext.getResultId();
        String token = sharedContext.getToken();
        StudyDetailResponse response = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get("/v1/studies/" + id)
                .then().log().all()
                .extract().as(StudyDetailResponse.class);

        assertAll(
                () -> assertThat(response.name()).isEqualTo("자바"),
                () -> assertThat(response.numberOfMaximumMembers()).isEqualTo(5)
        );
    }

    @Then("스터디의 회차를 조회할 수 있다.")
    public void 스터디의_회차를_조회할_수_있다() {
        String token = sharedContext.getToken();
        Long roundId = sharedContext.getRoundId();
        Long studyId = sharedContext.getResultId();

        ExtractableResponse<Response> response = given()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get("/v1/studies/" + studyId + "/rounds/" + roundId)
                .then().log().all().extract();

        RoundResponse round = response.as(RoundResponse.class);

        assertAll(
                () -> assertThat(round.masterId()).isEqualTo(valueOf(token)),
                () -> assertThat(round.id()).isEqualTo(roundId)
        );
    }

    @When("스터디의 {string} 회차를 찾는다.")
    public void 스터디의_회차를_찾는다(String roundNumber) {
        String token = sharedContext.getToken();
        Long id = sharedContext.getResultId();

        ExtractableResponse<Response> response = given()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get("/v1/studies/" + id)
                .then().extract();

        StudyDetailResponse studyDetailResponse = response.as(StudyDetailResponse.class);

        Long roundId = studyDetailResponse.rounds().stream()
                .filter(it -> Objects.equals(it.number(), Integer.valueOf(roundNumber)))
                .findFirst().get()
                .id();

        sharedContext.setRoundId(roundId);
    }
}
