package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.domain.study.ProcessingStatus;
import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.RecruitingStudyResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class StudySteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;
    private final MemberRepository memberRepository;

    public StudySteps(ObjectMapper objectMapper, SharedContext sharedContext, MemberRepository memberRepository) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
        this.memberRepository = memberRepository;
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

        Member member = memberRepository.save(MemberFixture.김진우.toMemberWithoutId());

        RequestSpecification requestSpecification = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, member.getId())
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
        final ExtractableResponse<Response> response = when().get("/v1/studies/recruiting?page=" + page)
                                                             .then()
                                                             .log()
                                                             .all()
                                                             .extract();

        sharedContext.setResponse(response);
    }

    @Then("모집 중인 스터디를 확인할 수 있다.")
    public void 모집_중인_스터디를_확인할_수_있다() {
        ExtractableResponse<Response> response = sharedContext.getResponse();

        List<RecruitingStudyResponse> recruitingStudyResponses = response.body()
                                                                         .jsonPath()
                                                                         .get();
        Predicate<RecruitingStudyResponse> isRecruitingPredicate = recruitingStudyResponse -> recruitingStudyResponse.processingStatus() == ProcessingStatus.RECRUITING.getCode();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(recruitingStudyResponses).allMatch(isRecruitingPredicate)
        );
    }
}
