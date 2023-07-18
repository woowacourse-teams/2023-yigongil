package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.request.StudyCreateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
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

        RequestSpecification requestSpecification = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(request));

        sharedContext.setRequestSpecification(requestSpecification);
    }

    @Then("스터디 목록에서 해당 스터디를 확인할 수 있다.")
    public void 스터디목록에서_해당_스터디를_확인할_수_있다() {
        // TODO: 2023/07/18 스터디 조회 api 작성 후 수정
    }
}
