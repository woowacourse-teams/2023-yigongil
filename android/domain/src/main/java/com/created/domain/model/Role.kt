package com.created.domain.model

enum class Role(val index: Int) {
    MASTER(0), MEMBER(1), APPLICANT(2), NOTHING(3), GUEST(4);

    companion object {
        fun valueOf(index: Int): Role {
            return Role.values().find { role ->
                role.index == index
            } ?: throw IllegalAccessException("해당 역할을 찾을 수 없습니다.")
        }
    }
}
