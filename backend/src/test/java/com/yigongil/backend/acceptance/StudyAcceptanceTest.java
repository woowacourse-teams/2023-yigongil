package com.yigongil.backend.acceptance;

import com.yigongil.backend.request.StudyCreateRequest;
import com.yigongil.backend.response.StudyDetailResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;

public class StudyAcceptanceTest extends AcceptanceTest {

    @Test
    public void 스터디상세_조회에서_해당_스터디를_확인할_수_있다() {
        StudyCreateRequest request = new StudyCreateRequest("이름", 3, "1919.01.01", 10, "1d", "thro");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/v1/studies")
                .then().log().all();

        StudyDetailResponse response = RestAssured.given().log().all()
                .when()
                .get("/v1/studies/1")
                .then().log().all()
                .extract().as(StudyDetailResponse.class);

        assertAll(
                () -> response.id().equals(1L),
                () -> response.name()
        );
    }
}
