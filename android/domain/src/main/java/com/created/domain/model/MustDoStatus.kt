package com.created.domain.model

enum class MustDoStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED,
    ;

    companion object {
        fun of(status: String): MustDoStatus {
            return when (status) {
                "NOT_STARTED" -> NOT_STARTED
                "IN_PROGRESS" -> IN_PROGRESS
                "FINISHED" -> FINISHED
                else -> throw IllegalArgumentException("$status 해당하는 status가 존재하지 않습니다.")
            }
        }
    }
}
