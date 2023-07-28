package com.yigongil.backend.domain.studymember;

import lombok.Getter;

@Getter
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
}
