package com.created.team201.data.mapper

import com.created.team201.presentation.common.customview.dayofselector.DayOfWeek

fun DayOfWeek.toDomain(): String = when (this) {
    DayOfWeek.MONDAY -> "MONDAY"
    DayOfWeek.TUESDAY -> "TUESDAY"
    DayOfWeek.WEDNESDAY -> "WEDNESDAY"
    DayOfWeek.THURSDAY -> "THURSDAY"
    DayOfWeek.FRIDAY -> "FRIDAY"
    DayOfWeek.SATURDAY -> "SATURDAY"
    DayOfWeek.SUNDAY -> "SUNDAY"
}
