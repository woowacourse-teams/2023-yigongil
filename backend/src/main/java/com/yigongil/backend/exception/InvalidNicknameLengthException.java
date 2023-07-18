package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class InvalidNicknameLengthException extends HttpException {

    public InvalidNicknameLengthException(String message, String input) {
        super(HttpStatus.BAD_REQUEST, "닉네임 길이는 2자 이상 8자 이하여야 합니다", input);
    }
}
