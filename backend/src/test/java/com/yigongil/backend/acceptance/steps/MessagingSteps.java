package com.yigongil.backend.acceptance.steps;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yigongil.backend.domain.event.MustDoUpdatedEvent;
import com.yigongil.backend.domain.event.StudyAppliedEvent;
import com.yigongil.backend.domain.event.StudyPermittedEvent;
import com.yigongil.backend.domain.event.StudyStartedEvent;
import com.yigongil.backend.domain.round.RoundStatus;
import com.yigongil.backend.infra.MessagingService;
import com.yigongil.backend.request.MessagingTokenRequest;
import com.yigongil.backend.request.MustDoUpdateRequest;
import com.yigongil.backend.request.StudyStartRequest;
import com.yigongil.backend.response.MembersCertificationResponse;
import com.yigongil.backend.response.RoundResponse;
import io.cucumber.java.en.Then;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
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

    @Then("{string}가 {string} 스터디를 {string}에 진행되도록 하여 시작한다. 스터디 시작 알림이 발송된다.")
    public void 스터디시작_알림_발송(String masterGithubId, String studyName, String days) {
        String token = sharedContext.getToken(masterGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);
        StudyStartRequest request = new StudyStartRequest(
                Arrays.stream(days.split(",")).map(String::strip).toList());

        given().log().all()
               .header(HttpHeaders.AUTHORIZATION, token)
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(request)
               .when()
               .patch("/studies/" + studyId + "/start")
               .then().log().all();

        StudyStartedEvent studyStartedEvent = new StudyStartedEvent(
                Long.parseLong(studyId),
                studyName
        );
        verify(messagingService).sendNotificationOfStudyStarted(studyStartedEvent);
    }

    @Then("{string}가 {string} 스터디의 현재 주차에 {string}라는 머스트두를 추가한다. 머스트두 등록 알림이 발송된다.")
    public void 머스트두등록_알림_발송(String masterGithubId, String studyName, String mustDoContent) {
        String token = sharedContext.getToken(masterGithubId);
        String studyId = (String) sharedContext.getParameter(studyName);

        MembersCertificationResponse membersCertificationResponse = given().log().all()
                                                                           .header(HttpHeaders.AUTHORIZATION, token)
                                                                           .when()
                                                                           .get("/studies/" + studyId + "/certifications")
                                                                           .then().log().all()
                                                                           .extract()
                                                                           .as(MembersCertificationResponse.class);

        List<RoundResponse> roundResponses = given().log().all()
                                                    .header(HttpHeaders.AUTHORIZATION, token)
                                                    .when()
                                                    .get("/studies/" + studyId + "/rounds?weekNumber=" + membersCertificationResponse.upcomingRound().weekNumber())
                                                    .then().log().all()
                                                    .extract()
                                                    .response()
                                                    .jsonPath().getList(".", RoundResponse.class);

        RoundResponse round = roundResponses.stream()
                                            .filter(roundResponse -> roundResponse.status() == RoundStatus.IN_PROGRESS)
                                            .findAny()
                                            .get();

        MustDoUpdateRequest request = new MustDoUpdateRequest(mustDoContent);

        given().log().all()
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .body(request)
               .header(HttpHeaders.AUTHORIZATION, token)
               .when()
               .put("/rounds/{roundId}/todos", round.id())
               .then().log().all();

        MustDoUpdatedEvent mustDoUpdatedEvent = new MustDoUpdatedEvent(
                Long.valueOf(studyId),
                studyName,
                mustDoContent
        );
        verify(messagingService).sendNotificationOfMustDoUpdated(mustDoUpdatedEvent);
    }
}
