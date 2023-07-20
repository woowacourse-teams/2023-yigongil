package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NotTodoOwnerException extends HttpException {

    public NotTodoOwnerException(String message, String input) {
        super(HttpStatus.UNAUTHORIZED, message, input);
    }
}
