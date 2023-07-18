package com.yigongil.backend.exception;

public class RoundNotFoundException extends RuntimeException {
    public RoundNotFoundException() {
        super("존재하지 않는 회차입니다.");
    }
}
