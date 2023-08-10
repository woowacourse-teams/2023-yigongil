package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidReportException extends HttpException {

    public InvalidReportException(String message, Long memberId) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(memberId));
    }
}
