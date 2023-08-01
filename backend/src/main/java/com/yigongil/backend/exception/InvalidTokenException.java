package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends HttpException {

    public InvalidTokenException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
