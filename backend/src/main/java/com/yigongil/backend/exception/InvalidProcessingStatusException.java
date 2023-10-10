package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidProcessingStatusException extends HttpException {

    public InvalidProcessingStatusException(String message, String processingStatus) {
        super(HttpStatus.BAD_REQUEST, message, processingStatus);
    }

    public InvalidProcessingStatusException(String processingStatus) {
        super(HttpStatus.BAD_REQUEST, "잘못된 스터디 상태 입력입니다.", processingStatus);
    }
}
