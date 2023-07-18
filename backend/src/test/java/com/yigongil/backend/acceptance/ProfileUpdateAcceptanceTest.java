package com.yigongil.backend.acceptance;

import com.yigongil.backend.domain.member.Member;
import com.yigongil.backend.domain.member.MemberRepository;
import com.yigongil.backend.fixture.MemberFixture;
import com.yigongil.backend.request.ProfileUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

class ProfileUpdateAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 내_정보를_올바르게_작성하면_정보가_수정된다() {
        ProfileUpdateRequest request = new ProfileUpdateRequest("김김진진우우", "자기소개입니다.");

        Member save = memberRepository.save(MemberFixture.김진우.toMemberWithoutId());

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, String.valueOf(save.getId()))
                .body(request)
                .when()
                .patch("/v1/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
