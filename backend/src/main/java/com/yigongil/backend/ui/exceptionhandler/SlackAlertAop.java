package com.yigongil.backend.ui.exceptionhandler;

import static com.slack.api.webhook.WebhookPayloads.payload;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile(value = {"prod", "dev"})
@Aspect
@Component
public class SlackAlertAop {

    private final Slack slackClient;
    private final String webhookUrl;

    public SlackAlertAop(@Value("${slack.webhook-url}") String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.slackClient = Slack.getInstance();
    }

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.ExceptionHandler) && args(e, request)", argNames = "e,request")
    private void exceptionOccurs(Exception e, HttpServletRequest request) {
    }

    @Before(value = "exceptionOccurs(e, request)", argNames = "e,request")
    public void alertOnSlack(Exception e, HttpServletRequest request) {
        sendSlackAlertErrorLog(e, request);
    }

    private void sendSlackAlertErrorLog(Exception e, HttpServletRequest request) {
        try {
            slackClient.send(webhookUrl, payload(p -> p
                    .text("서버 에러 발생")
                    .attachments(
                            List.of(generateSlackAttachment(e, request))
                    )
            ));
        } catch (IOException error) {
            log.info("Slack 통신 예외 발생");
        }
    }

    private Attachment generateSlackAttachment(Exception e, HttpServletRequest request) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
        String xffHeader = request.getHeader("X-FORWARDED-FOR");
        return Attachment.builder()
                         .color("ff0000")
                         .title(requestTime + " 발생 에러 로그")
                         .fields(List.of(
                                         generateSlackField("Request IP", xffHeader == null ? request.getRemoteAddr() : xffHeader),
                                         generateSlackField("Request URL", request.getRequestURL() + " " + request.getMethod()),
                                         generateSlackField("Error Message", e.getMessage())
                                 )
                         )
                         .build();
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                    .title(title)
                    .value(value)
                    .valueShortEnough(false)
                    .build();
    }
}
