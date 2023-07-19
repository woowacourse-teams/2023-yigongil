package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class TodoAlreadyDoneException extends HttpException {

    public TodoAlreadyDoneException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
