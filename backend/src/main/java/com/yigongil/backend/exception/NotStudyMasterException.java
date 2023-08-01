package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NotStudyMasterException extends HttpException {

    public NotStudyMasterException(String message, String input) {
        super(HttpStatus.UNAUTHORIZED, message, input);
    }
}
