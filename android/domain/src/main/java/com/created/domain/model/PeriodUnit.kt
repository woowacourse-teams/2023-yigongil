package com.created.domain.model

enum class PeriodUnit(val type: Int) {
    DAY(0), WEEK(1);

    companion object {
        private const val ERROR_ARGUMENT = "존재하지 않는 타입입니다"

        fun valueOf(type: Int): PeriodUnit =
            when (type) {
                0 -> DAY
                1 -> WEEK
                else -> throw IllegalArgumentException(ERROR_ARGUMENT)
            }

        fun valueOf(input: Char): PeriodUnit {
            return when (input) {
                'd' -> DAY
                'w' -> WEEK
                else -> throw IllegalArgumentException(ERROR_ARGUMENT)
            }
        }
    }
}
