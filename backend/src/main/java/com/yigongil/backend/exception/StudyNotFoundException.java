package com.yigongil.backend.exception;

import org.springframework.http.HttpStatus;

public class StudyNotFoundException extends HttpException {

    public StudyNotFoundException(String message, Long studyId) {
        super(HttpStatus.NOT_FOUND, message, String.valueOf(studyId));
    }
}
