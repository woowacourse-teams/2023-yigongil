package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;
    private final String input;

    public HttpException(HttpStatus httpStatus, String message, String input) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.input = input;
    }

    public String getMessageWithInput() {
        return message + ": " + input;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getInput() {
        return input;
    }
}
