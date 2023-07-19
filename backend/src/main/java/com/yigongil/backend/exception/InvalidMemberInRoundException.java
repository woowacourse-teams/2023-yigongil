package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidMemberInRoundException extends HttpException {

    public InvalidMemberInRoundException(String message, Long input) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(input));
    }
}
