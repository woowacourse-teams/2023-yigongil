package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NoCertificationException extends HttpException {

    public NoCertificationException(String message, String value) {
        super(HttpStatus.BAD_REQUEST, message, value);
    }
}
