package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.round.RoundStatus;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.request.StudyStartRequest;
import com.yigongil.backend.request.StudyUpdateRequest;
import com.yigongil.backend.response.MemberCertificationResponse;
import com.yigongil.backend.response.MembersCertificationResponse;
import com.yigongil.backend.response.RoundResponse;
import com.yigongil.backend.response.StudyDetailResponse;
import com.yigongil.backend.response.StudyListItemResponse;
import com.yigongil.backend.response.StudyMemberResponse;
import com.yigongil.backend.response.UpcomingStudyResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Given("{string}가 제목-{string}, 정원-{string}명, 최소 주차-{string}주, 주당 진행 횟수-{string}회, 소개-{string}로 스터디를 개설한다.")
    public void 스터디를_개설한다(
            String masterGithubId,
            String name,
            String numberOfMaximumMembers,
            String minimumWeeks,
            String meetingDaysCountPerWeek,
            String introduction
    ) throws JsonProcessingException {
        StudyUpdateRequest request = new StudyUpdateRequest(
                name,
                Integer.parseInt(numberOfMaximumMembers),
                Integer.parseInt(minimumWeeks),
                Integer.parseInt(meetingDaysCountPerWeek),
                introduction
        );
        String token = sharedContext.getToken(masterGithubId);

        String location = given().log().all()
                                 .header(HttpHeaders.AUTHORIZATION, token)
                                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 .body(objectMapper.writeValueAsString(request))
                                 .when()
                                 .post("/studies")
                                 .then().log().all()
                                 .extract()
                                 .header(HttpHeaders.LOCATION);

        String studyId = location.substring(location.lastIndexOf("/") + 1);

        sharedContext.setParameter(name, studyId);
    }

    @When("모집 중인 스터디 탭을 클릭한다.")
    public void 모집_중인_스터디를_요청한다() {
        ExtractableResponse<Response> response = given().log().all()
                                                        .param("status", "recruiting")
                                                        .when()
                                                        .get("/studies")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("모집 중인 스터디를 확인할 수 있다.")
    public void 모집_중인_스터디를_확인할_수_있다() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<StudyListItemResponse> studyInfoRespons = response.jsonPath()
                                                               .getList(".", StudyListItemResponse.class);
        Predicate<StudyListItemResponse> isRecruitingPredicate = recruitingStudyResponse -> recruitingStudyResponse.processingStatus() == ProcessingStatus.RECRUITING.getCode();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(studyInfoRespons).isNotEmpty(),
                () -> assertThat(studyInfoRespons).allMatch(isRecruitingPredicate)
        );
    }

    @When("스터디 상세 조회에서 이름이 {string}인 스터디를 조회한다.")
    public void 스터디_조회(String studyName) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .when()
                                                        .get("/studies/" + sharedContext.getId(studyName))
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("스터디 상세조회 결과가 제목-{string}, 정원-{int}, 최소 주차-{int}주, 주당 진행 횟수-{int}회, 소개-{string}로 조회된다.")
    public void 스터디상세_조회에서_해당_스터디를_확인할_수_있다(
            String studyName,
            int maximumNumber,
            int minimumWeeks,
            int meetingDaysCountPerWeek,
            String introduction
    ) {
        StudyDetailResponse response = sharedContext.getResponse()
                                                    .as(StudyDetailResponse.class);

        assertAll(
                () -> assertThat(response.name()).isEqualTo(studyName),
                () -> assertThat(response.numberOfMaximumMembers()).isEqualTo(maximumNumber),
                () -> assertThat(response.introduction()).isEqualTo(introduction),
                () -> assertThat(response.meetingDaysCountPerWeek()).isEqualTo(meetingDaysCountPerWeek),
                () -> assertThat(response.minimumWeeks()).isEqualTo(minimumWeeks)
        );
    }

    @Then("모집 중인 스터디를 {int}개 중 {int}개를 확인할 수 있다.")
    public void 모집_중인_스터디를_확인할수_있다(int totalCount, int acceptedCount) { // TODO 이거 확인점요 ㅎㅎ.
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<StudyListItemResponse> studyInfoRespons = response.jsonPath()
                                                               .getList(".", StudyListItemResponse.class);

        Predicate<StudyListItemResponse> isRecruitingPredicate = recruitingStudyResponse -> recruitingStudyResponse.processingStatus() == ProcessingStatus.RECRUITING.getCode();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(studyInfoRespons).allMatch(isRecruitingPredicate),
                () -> assertThat(studyInfoRespons).hasSize(acceptedCount)
        );
    }

    @When("{string}가 이름이 {string}인 스터디의 이번 회차 인증 정보를 조회한다.")
    public void 스터디_인증_정보_조회(String memberGithubId, String studyName) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION,
                                                                token)
                                                        .when()
                                                        .get("/studies/" + studyId + "/certifications")
                                                        .then().log().all()
                                                        .extract();
        sharedContext.setResponse(response);
    }

    @When("{string}가 이름이 {string}인 스터디의 {int} 주차를 조회한다.")
    public void 스터디_주차_조회(String memberGithubId, String studyName, int weekNumber) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/studies/" + studyId + "/rounds?weekNumber=" + weekNumber)
                                                        .then().log().all()
                                                        .extract();
        sharedContext.setResponse(response);
    }

    @When("{string}가 이름이 {string}인 스터디의 현재 주차를 통해 현재 회차를 찾는다.")
    public void 스터디_회차_조회(String memberGithubId, String studyName) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        MembersCertificationResponse membersCertificationResponse = given().log().all()
                                                                           .header(HttpHeaders.AUTHORIZATION,
                                                                                   token)
                                                                           .when()
                                                                           .get("/studies/" + studyId + "/certifications")
                                                                           .then().log().all()
                                                                           .extract()
                                                                           .as(MembersCertificationResponse.class);

        List<RoundResponse> roundResponses = given().log().all()
                                                    .header(HttpHeaders.AUTHORIZATION, token)
                                                    .when()
                                                    .get("/studies/" + studyId + "/rounds?weekNumber=" + membersCertificationResponse.upcomingRound().weekNumber())
                                                    .then().log().all()
                                                    .extract()
                                                    .response()
                                                    .jsonPath().getList(".", RoundResponse.class);

        RoundResponse round = roundResponses.stream()
                                            .filter(roundResponse -> roundResponse.status() == RoundStatus.IN_PROGRESS)
                                            .findAny()
                                            .get();

        sharedContext.setParameter("round", round);
        sharedContext.setParameter("roundId", round.id());
    }

    @Given("{string}가 이름이 {string}인 스터디를 {string}에 진행되도록 하여 시작한다.")
    public void 스터디_시작(String memberGithubId, String studyName, String days) {
        String token = sharedContext.getToken(memberGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);
        StudyStartRequest request = new StudyStartRequest(Arrays.stream(days.split(",")).map(String::strip).toList());

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(request)
               .when()
               .patch("/studies/" + studyId + "/start")
               .then().log().all();

        sharedContext.setParameter("currentRoundNumber", 1);
    }

    @When("{string}가 홈화면을 조회한다.")
    public void 현재_회차를_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/home")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Given("{string}가 {string} 스터디의 정보를 제목-{string}, 정원-{string}명, 최소 주차-{string}주, 주당 진행 횟수-{string}회, 소개-{string}로 수정한다.")
    public void 스터디_정보_수정(
            String masterGithubId,
            String originalStudyName,
            String updateStudyName,
            String updateNumberOfMaximumMembers,
            String updateMinimumWeeks,
            String updateMeetingDaysCountPerWeek,
            String updateIntroduction
    ) {
        String token = sharedContext.getToken(masterGithubId);
        String studyId = (String) sharedContext.getParameter(originalStudyName);

        StudyUpdateRequest request = new StudyUpdateRequest(
                updateStudyName,
                Integer.parseInt(updateNumberOfMaximumMembers),
                Integer.parseInt(updateMinimumWeeks),
                Integer.parseInt(updateMeetingDaysCountPerWeek),
                updateIntroduction
        );

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(request)
               .when()
               .put("/studies/{studyId}", studyId)
               .then().log().all();

        sharedContext.setParameter(updateStudyName, studyId);
    }

    @Then("스터디의 남은 날짜가 0이상 6 이하이다.")
    public void 스터디_회차_업데이트_검증() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<UpcomingStudyResponse> homeResponse = response.jsonPath().getList(".", UpcomingStudyResponse.class);

        assertThat(homeResponse.get(0).leftDays()).isBetween(0, 6);
    }

    @When("{string}를 검색한다.")
    public void 검색한다(String search) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .queryParam("search", search)
                                                        .queryParam("page", 0)
                                                        .when()
                                                        .get("/studies")
                                                        .then().log().all().extract();

        sharedContext.setResponse(response);
    }

    @Then("결과가 모두 {string}를 포함하고 {int} 개가 조회된다.")
    public void 결과가_모두_검색어를_포함한다(String search, int number) {
        List<StudyListItemResponse> responses = sharedContext.getResponse()
                                                             .jsonPath()
                                                             .getList(".", StudyListItemResponse.class);

        assertAll(
                () -> assertThat(responses).map(StudyListItemResponse::name).allMatch(name -> name.contains(search)),
                () -> assertThat(responses).hasSize(number)
        );
    }

    @Then("스터디가 {int} 개 조회된다.")
    public void 스터디가_int개_조회된다(int number) {
        List<UpcomingStudyResponse> response = sharedContext.getResponse()
                                                            .jsonPath()
                                                            .getList(".", UpcomingStudyResponse.class);

        assertThat(response).hasSize(number);
    }

    @When("{string}가 {string} 스터디를 종료한다.")
    public void 스터디_종료(String githubId, String studyName) {
        String token = sharedContext.getToken(githubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .patch("/studies/{studyId}/end", studyId)
               .then().log().all();
    }

    @Then("{string}가 {string} 스터디를 종료할 수 없다.")
    public void 스터디_종료_실패(String githubId, String studyName) {
        String token = sharedContext.getToken(githubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .patch("/studies/{studyId}/end", studyId)
               .then().log().all()
               .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @When("{string}이 지원한 스터디 목록을 조회한다.")
    public void 지원한_스터디_목록을_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/studies/waiting")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @When("{string}이 수락된 대기중 스터디 목록을 조회한다.")
    public void 대기중_스터디_목록을_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .param("role", "STUDY_MEMBER")
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/studies/waiting")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{int} 개의 스터디를 확인할 수 있다.")
    public void 스터디_n개를_확인할_수_있다(int count) {
        List<StudyListItemResponse> response = sharedContext.getResponse().jsonPath().getList(".", StudyListItemResponse.class);

        assertThat(response).hasSize(count);
    }

    @When("{string}이 지원한 스터디 목록을 {string} 검색어와 조회한다.")
    public void 지원한_스터디를_검색한다(String githubId, String search) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .param("search", search)
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/studies/waiting")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @When("스터디 목록을 {string} 검색어와 조회한다.")
    public void 스터디_목록을_검색한다(String search) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .param("search", search)
                                                        .when()
                                                        .get("/studies")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("현재 주차가 존재하고 {string}에 종료된다.")
    public void 현재_주차_종료일을_검증한다(String dayOfWeek) {
        ExtractableResponse<Response> response = sharedContext.getResponse();
        List<RoundResponse> roundResponses = response.response().jsonPath().getList(".", RoundResponse.class);
        Optional<RoundResponse> upcomingRound = roundResponses.stream()
                                                              .filter(roundResponse -> roundResponse.status() == RoundStatus.IN_PROGRESS)
                                                              .findAny();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(upcomingRound).isPresent(),
                () -> assertThat(upcomingRound.get().dayOfWeek().name()).isEqualTo(dayOfWeek)
        );
    }

    @Then("{string}가 이번 회차를 완료했다.")
    public void 이번_회차_완료_검증(String nickname) {
        ExtractableResponse<Response> extractableResponse = sharedContext.getResponse();
        MembersCertificationResponse response = extractableResponse.as(MembersCertificationResponse.class);
        MemberCertificationResponse myCertificationResponse = response.me();
        Optional<MemberCertificationResponse> memberCertificationResponse = response.others()
                                                                                    .stream()
                                                                                    .filter(member -> member.nickname().equals(nickname))
                                                                                    .findAny();
        MemberCertificationResponse certificationResponse = memberCertificationResponse.orElse(myCertificationResponse);

        assertAll(
                () -> assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(certificationResponse.isCertified()).isTrue()
        );
    }

    @Then("현재 회차의 머스트두가 {string}임을 확인할 수 있다.")
    public void 현재_주차_머스트두_검증(String content) {
        RoundResponse upcomingRound = (RoundResponse) sharedContext.getParameter("round");

        assertThat(upcomingRound.mustDo()).isEqualTo(content);
    }

    @When("{string} 이 {string} 스터디에서 탈퇴한다.")
    public void 스터디에서_탈퇴한다(String githubId, String studyName) {
        String token = sharedContext.getToken(githubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .delete("/studies/{studyId}/exit", studyId)
               .then().log().all();

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/studies/{studyId}/", studyId)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string} 이 {string} 스터디에 참여하지 않는다.")
    public void 스터디에_참여하지_않는다(String githubId, String studyName) {
        Long id = sharedContext.getId(githubId);

        StudyDetailResponse response = sharedContext.getResponse().as(StudyDetailResponse.class);

        assertThat(response.members()).map(StudyMemberResponse::id).doesNotContain(id);
    }

    @Then("{string}는 {string} 스터디 구성원에 포함되어 있지않다.")
    public void 지원자_삭제_검증(String githubId, String studyName) {
        String token = sharedContext.getToken(githubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/studies/" + studyId)
                                                        .then().log().all()
                                                        .extract();

        StudyDetailResponse studyDetailResponse = response.as(StudyDetailResponse.class);

        Long id = sharedContext.getId(githubId);

        boolean isExist = studyDetailResponse.members().stream()
                                             .anyMatch(studyMemberResponse -> studyMemberResponse.id().equals(id));
        assertThat(isExist).isFalse();
    }

    @When("{string}가 홈을 조회한다.")
    public void 홈을_조회한다(String githubId) {
        String token = sharedContext.getToken(githubId);

        ExtractableResponse<Response> response = given().log().all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/home")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    @Then("{string}스터디의 잔디 누적량이 {int} 이다.")
    public void 스터디의잔디누적량이이다(String studyName, int grassCount) {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<UpcomingStudyResponse> homeResponse = response.jsonPath().getList(".", UpcomingStudyResponse.class);

        UpcomingStudyResponse studyResponse = homeResponse.stream()
                                                          .filter(upcomingStudyResponse -> upcomingStudyResponse.name().equals(studyName))
                                                          .findAny()
                                                          .get();

        assertThat(studyResponse.grassCount()).isEqualTo(grassCount);
    }
}
