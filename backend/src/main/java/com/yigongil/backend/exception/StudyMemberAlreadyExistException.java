package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class StudyMemberAlreadyExistException extends HttpException {

    public StudyMemberAlreadyExistException(String message, String memberId) {
        super(HttpStatus.BAD_REQUEST, message, memberId);
    }
}
