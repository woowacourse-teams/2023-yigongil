package com.yigongil.backend.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.request.MemberJoinRequest;
import com.yigongil.backend.request.ProfileUpdateRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ProfileSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;
    private final MemberRepository memberRepository;

    public ProfileSteps(ObjectMapper objectMapper, SharedContext sharedContext, MemberRepository memberRepository) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
        this.memberRepository = memberRepository;
    }

    @Given("{string}의 깃허브 아이디로 회원가입을 한다.")
    public void 깃허브_아이디로_회원가입을_한다(String githubId) throws JsonProcessingException {
        MemberJoinRequest request = new MemberJoinRequest(githubId);

        String locationHeader = RestAssured.given()
                                   .log()
                                   .all()
                                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                                   .body(objectMapper.writeValueAsString(request))
                                   .when()
                                   .post("/v1/members")
                                   .then()
                                   .log()
                                   .all()
                                   .extract()
                                   .header(HttpHeaders.LOCATION);

        sharedContext.setToken(locationHeader.substring(locationHeader.lastIndexOf("/") + 1));
    }

    @Given("토큰을 인증 헤더에 추가한다.")
    public void 토큰을_인증_헤더에_추가한다() {
        sharedContext.getRequestSpecification()
                     .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken());
    }

    @Given("닉네임 {string}과 간단 소개{string}을 정상적으로 입력한다")
    public void 닉네임_간단소개_입력(String nickname, String introduction) throws JsonProcessingException {
        ProfileUpdateRequest request = new ProfileUpdateRequest(nickname, introduction);

        RequestSpecification requestSpecification = RestAssured.given()
                                                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                               .body(objectMapper.writeValueAsString(request));

        sharedContext.setRequestSpecification(requestSpecification);
    }

    @Then("변경된 정보를 확인할 수 있다")
    public void 변경된_정보를_확인_할_수_있다() {
        // TODO: 2023/07/18 조회Api생성후리팩터링
    }
}
