package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidMemberSizeException extends HttpException {

    public InvalidMemberSizeException(String message, Integer numberOfMaximumMembers) {
        super(HttpStatus.BAD_REQUEST, message, String.valueOf(numberOfMaximumMembers));
    }
}
