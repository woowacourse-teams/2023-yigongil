package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NecessaryTodoNotExistException extends HttpException {

    public NecessaryTodoNotExistException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
