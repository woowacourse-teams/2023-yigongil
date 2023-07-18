package com.yigongil.backend.acceptance;

import static io.restassured.RestAssured.given;

import com.yigongil.backend.request.ProfileUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class ProfileUpdateAcceptanceTest extends AcceptanceTest {

    @Test
    void 내_정보를_올바르게_작성하면_정보가_수정된다() {
        ProfileUpdateRequest request = new ProfileUpdateRequest("김김진진우우", "자기소개입니다.");

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .patch("/v1/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
