package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NecessaryTodoAlreadyExistException extends HttpException {
    public NecessaryTodoAlreadyExistException(String message, String input) {
        super(HttpStatus.CONFLICT, message, input);
    }
}
