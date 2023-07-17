package com.created.team201.presentation.studyList.model

import com.created.domain.PeriodUnit

data class PeriodUiModel(
    val number: Int,
    val unit: PeriodUnit,
) {
    override fun toString(): String {
        return when (unit) {
            PeriodUnit.DAY -> "$number day"
            PeriodUnit.WEEK -> "$number week"
        }
    }
}
