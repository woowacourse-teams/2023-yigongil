package com.yigongil.backend.ui.exceptionhandler;

import static com.slack.api.webhook.WebhookPayloads.payload;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.yigongil.backend.exception.HttpException;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {


    private final InternalServerErrorMessageConverter internalServerErrorMessageConverter;
    private final Slack slackClient = Slack.getInstance();
    @Value("${slack.webhook-url}")
    private String webhookUrl;

    public ApiExceptionHandler(InternalServerErrorMessageConverter internalServerErrorMessageConverter) {
        this.internalServerErrorMessageConverter = internalServerErrorMessageConverter;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleHttpException(HttpException e, HttpServletRequest request) {
        sendSlackAlertErrorLog(e, request);
        log.error("예외 발생: ", e);
        return ResponseEntity.status(e.getHttpStatus())
                             .body(e.getMessageWithInput());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleExpiredJwt(ExpiredJwtException e, HttpServletRequest request) {
        sendSlackAlertErrorLog(e, request);
        log.info("토큰 만료: ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("토큰이 만료되었습니다");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        sendSlackAlertErrorLog(e, request);
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) {
        sendSlackAlertErrorLog(e, request);
        log.error("예상치 못한 예외 발생: ", e);
        return ResponseEntity.internalServerError()
                             .body(internalServerErrorMessageConverter.convert(e));
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
