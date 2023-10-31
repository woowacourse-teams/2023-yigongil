package com.yigongil.backend.infra;

import com.yigongil.backend.domain.event.CertificationCreatedEvent;
import com.yigongil.backend.domain.event.FeedPostCreatedEvent;
import com.yigongil.backend.domain.event.MustDoUpdatedEvent;
import com.yigongil.backend.domain.event.StudyAppliedEvent;
import com.yigongil.backend.domain.event.StudyCreatedEvent;
import com.yigongil.backend.domain.event.StudyPermittedEvent;
import com.yigongil.backend.domain.event.StudyStartedEvent;
import com.yigongil.backend.domain.member.Member;

public interface MessagingService {

    void registerToken(String token, Member member);

    void registerStudyMaster(StudyCreatedEvent event);

    void sendNotificationOfApplicant(StudyAppliedEvent event);

    void sendNotificationOfPermitted(StudyPermittedEvent event);

    void sendNotificationOfStudyStarted(StudyStartedEvent event);

    void sendNotificationOfMustDoUpdated(MustDoUpdatedEvent event);

    void sendNotificationOfCertificationCreated(CertificationCreatedEvent event);

    void sendNotificationOfFeedPostCreated(FeedPostCreatedEvent event);
}
