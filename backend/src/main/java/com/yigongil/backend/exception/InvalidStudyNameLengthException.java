package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidStudyNameLengthException extends HttpException {

    public InvalidStudyNameLengthException(String message, int inputLength) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(inputLength));
    }
}
