package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidTodoLengthException extends HttpException {

    public InvalidTodoLengthException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
