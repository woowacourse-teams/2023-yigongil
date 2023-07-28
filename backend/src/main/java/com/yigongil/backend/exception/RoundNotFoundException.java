package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class RoundNotFoundException extends HttpException {

    public RoundNotFoundException(String message, Long input) {
        super(HttpStatus.NOT_FOUND, message, String.valueOf(input));
    }
}
