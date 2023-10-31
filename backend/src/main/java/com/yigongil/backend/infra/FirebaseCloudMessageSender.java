package com.yigongil.backend.infra;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"prod", "dev"})
@Component
public class FirebaseCloudMessageSender implements MessageSender {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseCloudMessageSender(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void subscribeToTopicAsync(List<String> tokens, String topic) {
        firebaseMessaging.subscribeToTopicAsync(tokens, topic);
    }

    @Override
    public void send(String message, String topic) {
        Message topicMessage = Message.builder()
                                      .setTopic(topic)
                                      .putData("message", message)
                                      .build();
        firebaseMessaging.sendAsync(topicMessage);
    }
}
