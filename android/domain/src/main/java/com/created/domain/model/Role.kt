package com.created.domain.model

enum class Role {
    MASTER, STUDY_MEMBER, APPLICANT, NOTHING, GUEST;

    companion object {
        fun valueOf(index: Int): Role {
            return Role.values().find { role: Role -> role.ordinal == index }
                ?: throw IllegalArgumentException("해당 역할을 찾을 수 없습니다.")
        }
    }
}
