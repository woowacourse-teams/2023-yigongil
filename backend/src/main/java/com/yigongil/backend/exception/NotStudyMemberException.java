package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NotStudyMemberException extends HttpException {

    public NotStudyMemberException(String message, String nickname) {
        super(HttpStatus.UNAUTHORIZED, message, nickname);
    }
}
