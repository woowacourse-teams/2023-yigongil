package com.yigongil.backend.acceptance.steps;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpHeaders;

public class ApplySteps {

    private final SharedContext sharedContext;
    private final MemberRepository memberRepository;

    public ApplySteps(SharedContext sharedContext, MemberRepository memberRepository) {
        this.sharedContext = sharedContext;
        this.memberRepository = memberRepository;
    }

    @Given("스터디 지원자 정보를 입력한다.")
    public void 회원정보_스터디정보_입력() {
        Member member = memberRepository.save(MemberFixture.폰노이만.toMemberWithoutId());

        RequestSpecification requestSpecification = RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, member.getId());

        sharedContext.setRequestSpecification(requestSpecification);
    }

    @Then("Member가 Study에 참여 신청을 한다.")
    public void 참여_신청() {
        // TODO: 2023/07/18 스터디 지원자 조회 API 생성 후 구현 예정
    }
}
