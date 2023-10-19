package com.created.team201.presentation.common.customview.dayofselector

import androidx.annotation.StringRes
import com.created.team201.R

enum class DayOfWeek(@StringRes val stringRes: Int) {
    MONDAY(R.string.monday),
    TUESDAY(R.string.tuesday),
    WEDNESDAY(R.string.wednesday),
    THURSDAY(R.string.thursday),
    FRIDAY(R.string.friday),
    SATURDAY(R.string.saturday),
    SUNDAY(R.string.sunday),
    ;

    companion object {
        fun getValuesWithStartDay(startDay: DayOfWeek = MONDAY): List<DayOfWeek> {
            val values = DayOfWeek.values()
            return values.slice(startDay.ordinal until values.size) + values.slice(0 until startDay.ordinal)
        }

        fun of(dayOfWeek: String): DayOfWeek {
            return when (dayOfWeek) {
                "MONDAY" -> MONDAY
                "TUESDAY" -> TUESDAY
                "WEDNESDAY" -> WEDNESDAY
                "THURSDAY" -> THURSDAY
                "FRIDAY" -> FRIDAY
                "SATURDAY" -> SATURDAY
                "SUNDAY" -> SUNDAY
                else -> throw IllegalArgumentException("${dayOfWeek}은 없는 요일입니다.")
            }
        }
    }
}
