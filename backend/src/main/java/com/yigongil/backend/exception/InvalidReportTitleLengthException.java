package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidReportTitleLengthException extends HttpException {

    public InvalidReportTitleLengthException(String message, String inputLength) {
        super(HttpStatus.BAD_REQUEST, message, inputLength);
    }
}
