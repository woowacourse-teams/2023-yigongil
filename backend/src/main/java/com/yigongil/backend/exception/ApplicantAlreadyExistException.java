package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class ApplicantAlreadyExistException extends HttpException {

    public ApplicantAlreadyExistException(String message, String memberId) {
        super(HttpStatus.BAD_REQUEST, message, memberId);
    }
}
