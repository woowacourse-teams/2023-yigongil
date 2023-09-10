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

public class StudyMemberSteps {

    private final SharedContext sharedContext;
    private Map<String, Integer> codeByRole;

    public StudyMemberSteps(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
    }

    @Given("역할에 따른 코드 표")
    public void 역할_코드(Map<String, Integer> codeByRole) {
        for (String role : codeByRole.keySet()) {
            System.out.println("role = " + role);
            System.out.println("codeByRole = " + codeByRole.get(role));
        }
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

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualRoleCode).isEqualTo(expectedRoleCode);
    }


//
//    @Then("역할이 스터디장으로 조회된다.")
//    public void 역할_스터디장_검증() {
//        ExtractableResponse<Response> response = sharedContext.getResponse();
//        StudyMemberRoleResponse studyMemberRole = response.as(StudyMemberRoleResponse.class);
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(studyMemberRole.role()).isEqualTo(0);
//    }
//
//    @Then("역할이 스터디 신청자로 조회된다.")
//    public void 역할_스터디신청자_검증(Map<String, Integer> roleList) {
//        System.out.println("roleList.size() = " + roleList.size());
//        ExtractableResponse<Response> response = sharedContext.getResponse();
//        StudyMemberRoleResponse studyMemberRole = response.as(StudyMemberRoleResponse.class);
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(studyMemberRole.role()).isEqualTo(2);
//    }
}
