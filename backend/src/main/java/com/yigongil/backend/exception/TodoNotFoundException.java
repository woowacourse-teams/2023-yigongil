package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class TodoNotFoundException extends HttpException {

    public TodoNotFoundException(String message, String input) {
        super(HttpStatus.NOT_FOUND, message, input);
    }
}
