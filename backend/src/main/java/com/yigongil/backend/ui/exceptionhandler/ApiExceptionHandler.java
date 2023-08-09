package com.yigongil.backend.ui.exceptionhandler;

import com.yigongil.backend.exception.HttpException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private final InternalServerErrorMessageConverter internalServerErrorMessageConverter;

    public ApiExceptionHandler(InternalServerErrorMessageConverter internalServerErrorMessageConverter) {
        this.internalServerErrorMessageConverter = internalServerErrorMessageConverter;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleHttpException(HttpException e) {
        log.error("예외 발생: ", e);
        return ResponseEntity.status(e.getHttpStatus())
                             .body(e.getMessageWithInput());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleExpiredJwt(ExpiredJwtException e) {
        log.info("토큰 만료: ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("토큰이 만료되었습니다");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        log.error("예상치 못한 예외 발생: ", e);
        return ResponseEntity.internalServerError()
                             .body(internalServerErrorMessageConverter.convert(e));
    }
}
