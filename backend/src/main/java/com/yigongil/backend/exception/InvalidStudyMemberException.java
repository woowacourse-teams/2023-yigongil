package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidStudyMemberException extends HttpException {

    public InvalidStudyMemberException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
