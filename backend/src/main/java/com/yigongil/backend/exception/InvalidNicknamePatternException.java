package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidNicknamePatternException extends HttpException {

    public InvalidNicknamePatternException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
