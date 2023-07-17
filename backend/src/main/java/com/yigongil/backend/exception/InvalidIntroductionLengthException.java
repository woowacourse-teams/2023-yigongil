package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidIntroductionLengthException extends HttpException {

    public InvalidIntroductionLengthException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
