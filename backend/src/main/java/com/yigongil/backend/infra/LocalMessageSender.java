package com.yigongil.backend.infra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = {"test", "local"})
@Component
public class LocalMessageSender implements MessageSender {

    private final Map<String, Set<String>> tokensByTopic;

    public LocalMessageSender() {
        this.tokensByTopic = new HashMap<>();
    }

    @Override
    public void subscribeToTopicAsync(List<String> tokens, String topic) {
        Set<String> savedTokens = tokensByTopic.computeIfAbsent(topic, key -> new HashSet<>());
        savedTokens.addAll(tokens);
    }

    @Override
    public void send(String message, String topic) {
        System.out.println("LocalMessageSender: " + message + " to " + topic);
    }
}
