package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.event.StudyAppliedEvent;
import com.yigongil.backend.domain.event.StudyPermittedEvent;
import com.yigongil.backend.infra.MessagingService;
import com.yigongil.backend.request.MessagingTokenRequest;
import io.cucumber.java.en.Then;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MessagingSteps {

    private final ObjectMapper objectMapper;
    private final SharedContext sharedContext;
    private final MessagingService messagingService;

    public MessagingSteps(ObjectMapper objectMapper, SharedContext sharedContext, MessagingService messagingService) {
        this.objectMapper = objectMapper;
        this.sharedContext = sharedContext;
        this.messagingService = messagingService;
    }

    @Then("{string}가 토큰을 메시지 서비스에 등록한다.")
    public void 토큰_등록(String githubId) throws JsonProcessingException {
        String token = sharedContext.getToken(githubId);
        MessagingTokenRequest request = new MessagingTokenRequest(token);
        given().log()
               .all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(objectMapper.writeValueAsString(request))
               .header("Authorization", token)
               .when()
               .post("/login/fake/messaging/tokens")
               .then()
               .statusCode(HttpStatus.OK.value())
               .log()
               .all();

        verify(messagingService).registerToken(token, sharedContext.getMember(githubId));
    }

    @Then("{string}가 {string}의 {string} 스터디 신청 알림을 받는다.")
    public void 신청_알림_발송(String masterGithubId, String applicantGithubId, String studyName) {
        String token = sharedContext.getToken(applicantGithubId);
        ExtractableResponse<Response> response = given().log()
                                                        .all()
                                                        .header(HttpHeaders.AUTHORIZATION, token)
                                                        .when()
                                                        .post("/studies/" + sharedContext.getParameter(studyName) + "/applicants")
                                                        .then()
                                                        .log()
                                                        .all()
                                                        .extract();

        sharedContext.setResponse(response);
        StudyAppliedEvent appliedEvent = new StudyAppliedEvent(
                sharedContext.getId(masterGithubId),
                studyName,
                applicantGithubId
        );
        verify(messagingService).sendNotificationOfApplicant(appliedEvent);
    }

    @Then("{string}가 {string}의 신청을 수락하고, {string} 스터디 신청 수락 알림을 받는다.")
    public void 신청수락_알림_발송(
            String masterGithubId,
            String permittedMemberGithubId,
            String studyName
    ) {
        Object studyId = sharedContext.getParameter(studyName);
        Object memberId = sharedContext.getParameter(permittedMemberGithubId);

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, sharedContext.getToken(masterGithubId))
               .when()
               .patch("/studies/{studyId}/applicants/{memberId}", studyId, memberId)
               .then()
               .log().all()
               .extract();

        StudyPermittedEvent studyPermittedEvent = new StudyPermittedEvent(
                Long.parseLong(String.valueOf(memberId)),
                Long.parseLong(String.valueOf(studyId)),
                studyName
        );
        verify(messagingService).sendNotificationOfPermitted(studyPermittedEvent);
    }
}
