package com.created.domain

enum class PeriodUnit(val index: Int) {
    DAY(0), WEEK(1);

    companion object {
        fun valueOf(input: Char): PeriodUnit {
            return when (input) {
                'd' -> DAY
                'w' -> WEEK
                else -> throw IllegalAccessException()
            }
        }
    }
}
