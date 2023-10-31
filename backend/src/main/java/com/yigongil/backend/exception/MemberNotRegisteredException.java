package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class MemberNotRegisteredException extends HttpException {

    public MemberNotRegisteredException(final String message, final String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
