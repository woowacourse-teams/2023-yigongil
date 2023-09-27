package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.request.CertificationCreateRequest;
import com.yigongil.backend.request.FeedPostCreateRequest;
import com.yigongil.backend.response.CertificationResponse;
import com.yigongil.backend.response.FeedPostResponse;
import com.yigongil.backend.response.MemberCertificationResponse;
import com.yigongil.backend.response.MembersCertificationResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class FeedSteps {

    private final SharedContext sharedContext;

    public FeedSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @When("{string}가 {string}스터디 피드에 {string}의 글을 작성한다.")
    public void 스터디_피드에_일반_글을_작성_한다(String githubId, String studyName, String content) {
        String token = sharedContext.getToken(githubId);
        FeedPostCreateRequest request = new FeedPostCreateRequest(content, "https://yigongil.png");
        given().log()
               .all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(request)
               .when()
               .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/feeds")
               .then()
               .log()
               .all()
               .statusCode(HttpStatus.OK.value());
    }

    @When("{string}가 {string}스터디 피드에 {string}의 인증 글을 작성한다.")
    public void 스터디_피드에_인증_글을_작성_한다(String githubId, String studyName, String content) {

        String token = sharedContext.getToken(githubId);
        CertificationCreateRequest request = new CertificationCreateRequest(content, "https://yigongil.png");
        final ExtractableResponse<Response> response = given().log()
                                                              .all()
                                                              .header(HttpHeaders.AUTHORIZATION, token)
                                                              .contentType("application/json")
                                                              .body(request)
                                                              .when()
                                                              .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/certifications")
                                                              .then()
                                                              .log()
                                                              .all()
                                                              .statusCode(HttpStatus.CREATED.value())
                                                              .extract();
        final String location = response.header("Location");
        sharedContext.setEntityId("certificationId", location.substring(location.lastIndexOf("/") + 1));
    }

    @Then("{string}가 {string}스터디 피드에서 {string}의 {string} 글을 확인할 수 있다.")
    public void 스터디_피드에서_글을_확인_할_수_있다(String githubId, String studyName, String author, String content) {
        String token = sharedContext.getToken(githubId);
        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/studies/" + sharedContext.getParameter(studyName) + "/feeds")
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .statusCode(HttpStatus.OK.value())
                                                        .extract();
        List<FeedPostResponse> responses = response.jsonPath().getList(".", FeedPostResponse.class);

        assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.get(0).author().nickname()).isEqualTo(author),
                () -> assertThat(responses.get(0).content()).isEqualTo(content)
        );
    }

    @Then("{string}가 {string}스터디 피드에서 {string}의 {string}의 인증 글을 확인할 수 있다.")
    public void 스터디_피드에서_인증_글을_확인_할_수_있다(String githubId, String studyName, String author, String content) {
        String token = sharedContext.getToken(githubId);
        Long certificationId = sharedContext.getId("certificationId");
        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/studies/" + sharedContext.getParameter(studyName) + "/certifications/" + certificationId)
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .statusCode(HttpStatus.OK.value())
                                                        .extract();

        CertificationResponse certificationResponse = response.as(CertificationResponse.class);
        assertAll(
                () -> assertThat(certificationResponse.author().nickname()).isEqualTo(author),
                () -> assertThat(certificationResponse.content()).isEqualTo(content)
        );
    }

    @When("{string}가 {string} 스터디의 인증 목록을 조회한다.")
    public void 인증_목록을_조회한다(String githubId, String studyName) {

        String token = sharedContext.getToken(githubId);
        Object studyId = sharedContext.getParameter(studyName);

        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/studies/" + studyId + "/certifications")
                                                        .then().log().all()
                                                        .extract();
        sharedContext.setResponse(response);
    }

    @Then("인증이 {int} 개 올라왔다.")
    public void 인증_개수_검증(int count) {
        MembersCertificationResponse response = sharedContext.getResponse().as(MembersCertificationResponse.class);

        long result = response.others().stream()
                              .filter(MemberCertificationResponse::isCertified)
                              .count();

        if (response.me().isCertified()) {
            result++;
        }

        assertThat(result).isEqualTo(count);
    }
}
