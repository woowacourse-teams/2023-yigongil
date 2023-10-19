package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidImageExtensionException extends HttpException {

    public InvalidImageExtensionException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
