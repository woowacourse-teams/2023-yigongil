package com.yigongil.backend.response;

import com.yigongil.backend.domain.studymember.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudyMemberRoleResponse(
        @Schema(example = "1") int role
) {

    public static StudyMemberRoleResponse from(Role role) {
        return new StudyMemberRoleResponse(role.getCode());
    }
}
