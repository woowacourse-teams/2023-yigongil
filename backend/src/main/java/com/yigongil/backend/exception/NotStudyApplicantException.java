package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class NotStudyApplicantException extends HttpException {

    public NotStudyApplicantException(String message, Long applicantId) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(applicantId));
    }
}
