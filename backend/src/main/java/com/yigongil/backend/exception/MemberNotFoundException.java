package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends HttpException {

    public MemberNotFoundException(String message, String input) {
        super(HttpStatus.NOT_FOUND, message, input);
    }
}
