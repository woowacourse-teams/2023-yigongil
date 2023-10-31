package com.yigongil.backend.infra;

import java.util.List;

public interface MessageSender {

    void subscribeToTopicAsync(List<String> tokens, String topic);

    void send(String message, String topic);
}
