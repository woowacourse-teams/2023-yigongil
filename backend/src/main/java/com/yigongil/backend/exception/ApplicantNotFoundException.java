package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class ApplicantNotFoundException extends HttpException {

    public ApplicantNotFoundException(String message, Long memberId) {
        super(HttpStatus.NOT_FOUND, message, String.valueOf(memberId));
    }
}
