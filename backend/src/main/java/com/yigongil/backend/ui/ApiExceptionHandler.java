package com.yigongil.backend.ui;

import com.yigongil.backend.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<String> handleHttpException(HttpException e) {
        log.error("예외 발생: " + e);
        return ResponseEntity.status(e.getHttpStatus())
                .body(e.getMessageWithInput());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("예상치 못한 예외 발생: " + e);
        return ResponseEntity.internalServerError()
                .body("서버 에러 발생");
    }
}
