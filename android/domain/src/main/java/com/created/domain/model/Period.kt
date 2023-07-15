package com.created.domain.model

data class Period(
    val date: Int,
    val type: Int,
) {
    fun getPeriodDate(type: Int): Int {
        if (this.type == type) {
            return date
        }
        if (type == TYPE_DAY && this.type == TYPE_WEEK) {
            return date * WEEK_DAYS
        }
        return date / WEEK_DAYS
    }

    fun hasPeriodWeekType(): Boolean {
        return (getPeriodDate(TYPE_DAY) / WEEK_DAYS) >= CONDITION_HAS_WEEK
    }

    companion object {
        const val TYPE_DAY = 0
        const val TYPE_WEEK = 1
        private const val WEEK_DAYS = 7
        private const val CONDITION_HAS_WEEK = 1
    }
}
