package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.yigongil.backend.request.CertificationFeedPostCreateRequest;
import com.yigongil.backend.request.RegularFeedPostCreateRequest;
import com.yigongil.backend.response.FeedPostsResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class FeedSteps {

    private final SharedContext sharedContext;

    public FeedSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @When("{string}가 {string}스터디 피드에 {string}의 글을 작성한다.")
    public void 스터디_피드에_일반_글을_작성_한다(String githubId, String studyName, String content) {
        String token = sharedContext.getToken(githubId);
        RegularFeedPostCreateRequest request = new RegularFeedPostCreateRequest(content, "https://yigongil.png");
        given().log()
               .all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .body(request)
               .when()
               .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/posts/regular")
               .then()
               .log()
               .all()
               .statusCode(HttpStatus.OK.value());
    }

    @When("{string}가 {string}스터디 피드에 {string}의 인증 글을 작성한다.")
    public void 스터디_피드에_인증_글을_작성_한다(String githubId, String studyName, String content) {

        String token = sharedContext.getToken(githubId);
        CertificationFeedPostCreateRequest request = new CertificationFeedPostCreateRequest(content, "https://yigongil.png");
        given().log()
               .all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .body(request)
               .when()
               .post("/v1/studies/" + sharedContext.getParameter(studyName) + "/posts/certification")
               .then()
               .log()
               .all()
               .statusCode(HttpStatus.OK.value());
    }

    @Then("{string}가 {string}스터디 피드에서 {string}의 {string} 글을 확인할 수 있다.")
    public void 스터디_피드에서_글을_확인_할_수_있다(String githubId, String studyName, String author, String content) {
        String token = sharedContext.getToken(githubId);
        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/studies/" + sharedContext.getParameter(studyName) + "/posts")
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .statusCode(HttpStatus.OK.value())
                                                        .extract();
        FeedPostsResponse feedPostsResponse = response.as(FeedPostsResponse.class);

        assertAll(
                () -> assertThat(feedPostsResponse.regulars()).hasSize(1),
                () -> assertThat(feedPostsResponse.certifications()).hasSize(0),
                () -> assertThat(feedPostsResponse.regulars().get(0).author().nickname()).isEqualTo(author),
                () -> assertThat(feedPostsResponse.regulars().get(0).content()).isEqualTo(content)
        );
    }

    @Then("{string}가 {string}스터디 피드에서 {string}의 {string}의 인증 글을 확인할 수 있다.")
    public void 스터디_피드에서_인증_글을_확인_할_수_있다(String githubId, String studyName, String author, String content) {
        String token = sharedContext.getToken(githubId);
        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .get("/v1/studies/" + sharedContext.getParameter(studyName) + "/posts")
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .statusCode(HttpStatus.OK.value())
                                                        .extract();
        FeedPostsResponse feedPostsResponse = response.as(FeedPostsResponse.class);

        assertAll(
                () -> assertThat(feedPostsResponse.regulars()).hasSize(0),
                () -> assertThat(feedPostsResponse.certifications()).hasSize(1),
                () -> assertThat(feedPostsResponse.certifications().get(0).author().nickname()).isEqualTo(author),
                () -> assertThat(feedPostsResponse.certifications().get(0).content()).isEqualTo(content)
        );
    }
}
