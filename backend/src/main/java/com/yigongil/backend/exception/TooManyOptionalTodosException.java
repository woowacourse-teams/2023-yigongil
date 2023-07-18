package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class TooManyOptionalTodosException extends HttpException {

    public TooManyOptionalTodosException(String message, String input) {
        super(HttpStatus.CONFLICT, message, input);
    }
}
