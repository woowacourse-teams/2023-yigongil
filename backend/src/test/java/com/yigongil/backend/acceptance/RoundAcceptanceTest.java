package com.yigongil.backend.acceptance;

import com.yigongil.backend.request.StudyCreateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class RoundAcceptanceTest extends AcceptanceTest {

    @Test
    void 라운드_단일_조회를_한다() {

        스터디를_만든다();

        RestAssured.given().log().all()
                .when()
                .get("/v1/studies/1/rounds/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    private void 스터디를_만든다() {
        StudyCreateRequest studyCreateRequest = new StudyCreateRequest("스터디", 3, "1919.02.21",
                5, "1d", "설명입니다.");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(studyCreateRequest)
                .when().post("/v1/studies")
                .then().log().all();
    }
}
