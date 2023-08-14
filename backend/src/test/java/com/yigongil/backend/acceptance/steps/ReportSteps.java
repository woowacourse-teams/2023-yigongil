package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.yigongil.backend.config.oauth.JwtTokenProvider;
import com.yigongil.backend.request.MemberReportCreateRequest;
import com.yigongil.backend.request.StudyReportCreateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ReportSteps {

    private final SharedContext sharedContext;
    private final JwtTokenProvider jwtTokenProvider;

    public ReportSteps(SharedContext sharedContext, JwtTokenProvider jwtTokenProvider) {
        this.sharedContext = sharedContext;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Given("피신고자-{string}, 신고 제목-{string}, 내용-{string}, 문제발생일-{string}일 전으로 멤버 신고 정보를 작성한다.")
    public void 멤버_신고_폼_작성(String reportedMember, String title, String content, String days) {
        String reportedMemberToken = (String) sharedContext.getParameter(reportedMember);
        Long reportedMemberId = jwtTokenProvider.parseToken(reportedMemberToken);

        LocalDate problemOccuredAt = LocalDate.now().minusDays(Long.parseLong(days));

        MemberReportCreateRequest request = new MemberReportCreateRequest(
                reportedMemberId,
                title,
                content,
                problemOccuredAt
        );
        sharedContext.setParameter(title, request);
    }

    @Given("신고 스터디 이름-{string}, 신고 제목-{string}, 내용-{string}, 문제발생일-{string}일 전으로 스터디 신고 정보를 작성한다.")
    public void 스터디_신고_폼_작성(String reportedStudy, String title, String content, String days) {
        Long reportedStudyId = Long.valueOf((String) sharedContext.getParameter(reportedStudy));
        LocalDate problemOccuredAt = LocalDate.now().minusDays(Long.parseLong(days));

        StudyReportCreateRequest request = new StudyReportCreateRequest(
                reportedStudyId,
                title,
                content,
                problemOccuredAt
        );
        sharedContext.setParameter(title, request);
    }

    @When("{string}가 제목이 {string}인 신고 정보를 통해 멤버 신고 요청을 한다.")
    public void 유저_신고(String reporter, String reportTitle) {
        String reporterToken = (String) sharedContext.getParameter(reporter);
        MemberReportCreateRequest request = (MemberReportCreateRequest) sharedContext.getParameter(reportTitle);

        sharedContext.setResponse(given().log().all()
                                         .header(HttpHeaders.AUTHORIZATION, reporterToken)
                                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .body(request)
                                         .when()
                                         .post("/v1/reports/by-member")
                                         .then().log().all()
                                         .extract());
    }

    @When("{string}가 제목이 {string}인 신고 정보를 통해 스터디 신고 요청을 한다.")
    public void 스터디_신고(String reporter, String reportTitle) {
        String reporterToken = (String) sharedContext.getParameter(reporter);
        StudyReportCreateRequest request = (StudyReportCreateRequest) sharedContext.getParameter(reportTitle);

        sharedContext.setResponse(given().log().all()
                                         .header(HttpHeaders.AUTHORIZATION, reporterToken)
                                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .body(request)
                                         .when()
                                         .post("/v1/reports/by-study")
                                         .then().log().all()
                                         .extract());
    }

    @Then("신고에 성공한다.")
    public void 신고_응답_상태코드_확인() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
