package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public enum CustomError {

    INVALID_NICKNAME_LENGTH (HttpStatus.BAD_REQUEST, "NICKNAME_LENGTH"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;

    CustomError(HttpStatus httpStatus, String errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
