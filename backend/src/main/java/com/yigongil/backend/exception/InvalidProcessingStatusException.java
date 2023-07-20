package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidProcessingStatusException extends HttpException {

    public InvalidProcessingStatusException(String message, String processingStatus) {
        super(HttpStatus.BAD_REQUEST, message, processingStatus);
    }
}
