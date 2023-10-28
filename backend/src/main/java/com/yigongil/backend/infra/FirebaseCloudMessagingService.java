package com.yigongil.backend.infra;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.yigongil.backend.domain.event.CertificationCreatedEvent;
import com.yigongil.backend.domain.event.FeedPostCreatedEvent;
import com.yigongil.backend.domain.event.MustDoUpdatedEvent;
import com.yigongil.backend.domain.event.StudyAppliedEvent;
import com.yigongil.backend.domain.event.StudyCreatedEvent;
import com.yigongil.backend.domain.event.StudyPermittedEvent;
import com.yigongil.backend.domain.event.StudyStartedEvent;
import com.yigongil.backend.domain.member.Member;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Profile(value = {"prod", "dev"})
@Component
public class FirebaseCloudMessagingService implements MessagingService {

    private final FirebaseMessaging firebaseMessaging;
    private final FirebaseMemberRepository firebaseMemberRepository;

    public FirebaseCloudMessagingService(FirebaseMessaging firebaseMessaging, final FirebaseMemberRepository firebaseMemberRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.firebaseMemberRepository = firebaseMemberRepository;
    }

    @Override
    public void registerToken(String token, Member member) {
        firebaseMemberRepository.save(new FirebaseMember(member, token));
        subscribeToTopic(List.of(token), getMemberTopicById(member.getId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerStudyMaster(StudyCreatedEvent event) {
        List<String> tokens = findAllTokensByMemberId(event.masterId());
        subscribeToTopic(tokens, getStudyTopicById(event.studyId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOfApplicant(StudyAppliedEvent event) {
        sendToMember(event.appliedMemberGithubId() + "님이 " + event.studyName() + " 스터디에 신청했어요!", event.studyMasterId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOfPermitted(StudyPermittedEvent event) {
        List<String> tokens = findAllTokensByMemberId(event.permittedMemberId());
        subscribeToTopic(tokens, getStudyTopicById(event.studyId()));
        sendToMember(event.studyName() + " 스터디 신청이 수락되었어요!", event.permittedMemberId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOfStudyStarted(StudyStartedEvent event) {
        sendToStudy(event.studyName() + " 스터디가 시작되었어요!", event.studyId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOfMustDoUpdated(MustDoUpdatedEvent event) {
        sendToStudy(event.studyName() + " 스터디의 머스트두가 " + event.mustDo() + "로 변경되었어요!", event.studyId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOfCertificationCreated(CertificationCreatedEvent event) {
        sendToStudy(event.studyName() + " 스터디에" + event.authorGithubId() + " 님의 머스트두 인증이 등록되었어요!", event.studyId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOfFeedPostCreated(FeedPostCreatedEvent event) {
        sendToStudy(event.studyName() + " 스터디에 " + event.authorGithubId() + " 님이 피드를 등록했어요!", event.studyId());
    }

    private List<String> findAllTokensByMemberId(Long memberId) {
        return firebaseMemberRepository.findAllByMemberId(memberId)
                                       .stream()
                                       .map(FirebaseMember::getToken)
                                       .toList();
    }

    public void subscribeToTopic(List<String> tokens, String topic) {
        firebaseMessaging.subscribeToTopicAsync(tokens, topic);
    }

    @Override
    public void sendToMember(String message, Long memberId) {
        doSend(message, getMemberTopicById(memberId));
    }

    private String getMemberTopicById(final Long memberId) {
        return "member " + memberId;
    }

    @Override
    public void sendToStudy(String message, Long studyId) {
        doSend(message, getStudyTopicById(studyId));
    }

    @NotNull
    private static String getStudyTopicById(final Long studyId) {
        return "study " + studyId;
    }

    private void doSend(String message, String topic) {
        Message tokenMessage = Message.builder()
                                      .setTopic(topic)
                                      .putData("message", message)
                                      .build();
        firebaseMessaging.sendAsync(tokenMessage);
    }
}
