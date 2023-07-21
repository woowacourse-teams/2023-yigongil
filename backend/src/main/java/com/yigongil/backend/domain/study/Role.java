package com.yigongil.backend.domain.study;

public enum Role {

    MASTER(0),
    STUDY_MEMBER(1),
    APPLICANT(2),
    NO_ROLE(3),
    ;

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
