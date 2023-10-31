package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidReportContentLengthException extends HttpException {

    public InvalidReportContentLengthException(String message, String inputLength) {
        super(HttpStatus.BAD_REQUEST, message, inputLength);
    }
}
