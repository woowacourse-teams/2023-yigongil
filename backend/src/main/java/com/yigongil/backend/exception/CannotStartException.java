package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class CannotStartException extends HttpException {

    public CannotStartException(String message, Long input) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(input));
    }
}
