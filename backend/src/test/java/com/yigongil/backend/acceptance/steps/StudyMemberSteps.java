package com.yigongil.backend.acceptance.steps;

import com.yigongil.backend.response.StudyMemberRoleResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StudyMemberSteps {

    private final SharedContext sharedContext;
    private Map<String, Integer> codeByRole;

    public StudyMemberSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @Given("역할에 따른 코드 표")
    public void 역할_코드(Map<String, Integer> codeByRole) {
        this.codeByRole = codeByRole;
    }

    @When("{string}의 {string} 스터디에서의 역할을 조회한다.")
    public void 스터디_멤버_역할_조회(String memberName, String studyName) {
        Long studyId = sharedContext.getId(studyName);
        String memberToken = sharedContext.getToken(memberName);
        ExtractableResponse<Response> response = given().when()
                                                        .header(HttpHeaders.AUTHORIZATION, memberToken)
                                                        .get("/v1/studies/{studyId}/members/role", studyId)
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .extract();
        sharedContext.setResponse(response);
    }

    @Then("역할이 {string}이다.")
    public void 역할_검증(String role) {
        Integer expectedRoleCode = codeByRole.get(role);
        ExtractableResponse<Response> response = sharedContext.getResponse();
        int actualRoleCode = response.as(StudyMemberRoleResponse.class).role();

        assertAll(
                () -> assertThat(actualRoleCode).isEqualTo(expectedRoleCode),
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }
}
