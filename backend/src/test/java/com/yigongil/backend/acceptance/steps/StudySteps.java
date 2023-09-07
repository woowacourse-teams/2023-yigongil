package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.response.HomeResponse;
import com.yigongil.backend.response.RecruitingStudyResponse;
import com.yigongil.backend.response.RoundNumberResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Given("{string}가 제목-{string}, 정원-{string}명, 예상시작일-{string}일 뒤, 총 회차-{string}회, 주기-{string}, 소개-{string}로 스터디를 개설한다.")
    public void 스터디를_개설한다(
            String masterGithubId,
            String name,
            String numberOfMaximumMembers,
            String leftDays,
            String totalRoundCount,
            String periodOfRound,
            String introduction
    ) throws JsonProcessingException {
        LocalDate startAt = LocalDate.now().plus(Long.parseLong(leftDays), ChronoUnit.DAYS);
        StudyUpdateRequest request = new StudyUpdateRequest(
                name,
                Integer.parseInt(numberOfMaximumMembers),
                startAt,
                Integer.parseInt(totalRoundCount),
                periodOfRound,
                introduction
        );
        String token = sharedContext.getToken(masterGithubId);

        String location = given().log().all()
                                 .header(HttpHeaders.AUTHORIZATION, token)
                                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 .body(objectMapper.writeValueAsString(request))
                                 .when()
                                 .post("/v1/studies")
                                 .then().log().all()
                                 .extract()
                                 .header(HttpHeaders.LOCATION);

        String studyId = location.substring(location.lastIndexOf("/") + 1);

        sharedContext.setParameter(name, studyId);
    }

    @When("모집 중인 스터디 탭을 클릭한다.")
    public void 모집_중인_스터디를_요청한다() {
        ExtractableResponse<Response> response = when().get("/v1/studies/recruiting?page=0")
                                                       .then().log().all()
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
                () -> assertThat(recruitingStudyResponses).isNotEmpty(),
                () -> assertThat(recruitingStudyResponses).allMatch(isRecruitingPredicate)
        );
    }

    @When("{string}가 스터디 상세 조회에서 이름이 {string}인 스터디를 조회한다.")
    public void 스터디_조회(String memberGithubId, String studyName) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(memberGithubId))
                                                        .when()
                                                        .get("/v1/studies/" + sharedContext.getParameter(studyName))
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("스터디 상세조회 결과가 제목-{string}, 정원-{string}로 조회된다.")
    public void 스터디상세_조회에서_해당_스터디를_확인할_수_있다(String studyName, String maximumNumber) {
        StudyDetailResponse response = sharedContext.getResponse()
                                                    .as(StudyDetailResponse.class);

        assertAll(
                () -> assertThat(response.name()).isEqualTo(studyName),
                () -> assertThat(
                        response.numberOfMaximumMembers()).isEqualTo(Integer.parseInt(maximumNumber)
                )
        );
    }

    @Then("모집 중인 스터디를 {int}개 중 {int}개를 확인할 수 있다.")
    public void 모집_중인_스터디를_확인할수_있다(int totalCount, int acceptedCount) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<RecruitingStudyResponse> recruitingStudyResponses = response.jsonPath()
                                                                         .getList(".", RecruitingStudyResponse.class);

        Predicate<RecruitingStudyResponse> isRecruitingPredicate = recruitingStudyResponse -> recruitingStudyResponse.processingStatus() == ProcessingStatus.RECRUITING.getCode();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(recruitingStudyResponses).allMatch(isRecruitingPredicate),
                () -> assertThat(recruitingStudyResponses).hasSize(acceptedCount)
        );
    }

    @Then("스터디 장이 {string}이고 해당 회차 인것을 확인할 수 있다.")
    public void 스터디의_회차를_조회할_수_있다(String masterGithubId) {
        RoundResponse round = sharedContext.getResponse()
                                           .as(RoundResponse.class);

        Long masterId = sharedContext.getId(masterGithubId);

        assertAll(
                () -> assertThat(round.masterId()).isEqualTo(masterId),
                () -> assertThat(round.id()).isEqualTo(sharedContext.getParameter("roundId"))
        );
    }


    @When("{string}가 이름이 {string}인 스터디의 {int} 회차를 찾는다.")
    public void 스터디_회차_조회(String memberGithubId, String studyName, int roundNumber) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        StudyDetailResponse studyDetailResponse = given().log().all()
                                                         .header(HttpHeaders.AUTHORIZATION,
                                                                 token)
                                                         .when()
                                                         .get("/v1/studies/" + studyId)
                                                         .then().log().all()
                                                         .extract()
                                                         .as(StudyDetailResponse.class);

        Long roundId = studyDetailResponse.rounds()
                                          .stream()
                                          .filter(round -> Objects.equals(round.number(),
                                                  roundNumber))
                                          .findFirst()
                                          .map(RoundNumberResponse::id)
                                          .get();

        sharedContext.setParameter("roundId", roundId);

        ExtractableResponse<Response> response = given()
                .header(HttpHeaders.AUTHORIZATION, token)
                .when()
                .get("/v1/studies/" + studyId + "/rounds/" + roundId)
                .then().log().all()
                .extract();

        sharedContext.setResponse(response);
    }

    @Given("{string}가 이름이 {string}인 스터디를 시작한다.")
    public void 스터디시작(String memberGithubId, String studyName) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .patch("/v1/studies/" + studyId + "/start")
               .then().log().all();

        sharedContext.setParameter("currentRoundNumber", 1);
    }

    @When("{string}가 홈화면을 조회한다.")
    public void 현재_회차를_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/home/")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Given("{string}가 {string} 스터디의 정보를 제목-{string}, 정원-{string}명, 예상시작일-{string}일 뒤, 총 회차-{string}회, 주기-{string}, 소개-{string}로 수정한다.")
    public void 스터디_정보_수정(
            String masterGithubId,
            String originalStudyName,
            String updateStudyName,
            String updateNumberOfMaximumMembers,
            String updateStartAt,
            String updateTotalRoundCount,
            String updatePeriodOfRound,
            String updateIntroduction
    ) {
        String token = sharedContext.getToken(masterGithubId);
        String studyId = (String) sharedContext.getParameter(originalStudyName);

        StudyUpdateRequest request = new StudyUpdateRequest(
                updateStudyName,
                Integer.parseInt(updateNumberOfMaximumMembers),
                LocalDate.now().plus(Long.parseLong(updateStartAt), ChronoUnit.DAYS),
                Integer.parseInt(updateTotalRoundCount),
                updatePeriodOfRound,
                updateIntroduction
        );

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(request)
               .when()
               .put("/v1/studies/{studyId}", studyId)
               .then().log().all();

        sharedContext.setParameter(updateStudyName, studyId);
    }

    @Then("스터디의 남은 날짜가 null이 아니다.")
    public void 스터디_회차_업데이트_검증() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        HomeResponse homeResponse = response.as(HomeResponse.class);

        assertThat(homeResponse.studies().get(0).nextDate()).isNotNull();
    }

    @When("{string}를 검색한다.")
    public void 검색한다(String search) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .queryParam("q", search)
                                                        .queryParam("page", 0)
                                                        .when()
                                                        .get("/v1/studies/recruiting/search")
                                                        .then().log().all().extract();

        sharedContext.setResponse(response);
    }

    @Then("결과가 모두 {string}를 포함하고 {int} 개가 조회된다.")
    public void 결과가_모두_검색어를_포함한다(String search, int number) {
        List<RecruitingStudyResponse> responses = sharedContext.getResponse()
                                                               .jsonPath()
                                                               .getList(".", RecruitingStudyResponse.class);

        assertAll(
                () -> assertThat(responses).map(RecruitingStudyResponse::name).allMatch(name -> name.contains(search)),
                () -> assertThat(responses).hasSize(number)
        );
    }

    @Then("스터디가 {int} 개 조회된다.")
    public void 스터디가_int개_조회된다(int number) {
        HomeResponse response = sharedContext.getResponse().as(HomeResponse.class);

        assertThat(response.studies()).hasSize(number);
    }
}
