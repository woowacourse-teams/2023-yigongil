package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class CannotEndException extends HttpException {

    public CannotEndException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
