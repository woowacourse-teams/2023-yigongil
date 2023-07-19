package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class TodoNotDoneYetException extends HttpException {

    public TodoNotDoneYetException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
