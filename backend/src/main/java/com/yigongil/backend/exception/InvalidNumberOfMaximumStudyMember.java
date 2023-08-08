package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidNumberOfMaximumStudyMember extends HttpException {

    public InvalidNumberOfMaximumStudyMember(String message, Integer numberOfMaximumMembers) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(numberOfMaximumMembers));
    }
}
