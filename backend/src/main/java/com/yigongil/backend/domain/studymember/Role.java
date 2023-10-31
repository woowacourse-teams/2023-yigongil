package com.yigongil.backend.domain.studymember;

import java.util.Arrays;
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

    public static Role of(String source) {
        return Arrays.stream(Role.values())
                     .filter(role -> role.name().equalsIgnoreCase(source))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("No such role: " + source));
    }
}
