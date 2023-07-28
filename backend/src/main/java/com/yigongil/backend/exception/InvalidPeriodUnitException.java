package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidPeriodUnitException extends HttpException {

    public InvalidPeriodUnitException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, message, input);
    }
}
