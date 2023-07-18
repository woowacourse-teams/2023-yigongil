package com.yigongil.backend.exception;

public class StudyNotFoundException extends RuntimeException {
    public StudyNotFoundException() {
        super("존재하지 않는 스터디입니다.");
    }
}
