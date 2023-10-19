package com.created.team201.presentation.studyThread.model

import com.created.domain.model.MustDoStatus
import com.created.team201.presentation.common.customview.dayofselector.DayOfWeek

data class WeeklyMustDoUiModel(
    val id: Long,
    val mustDo: String,
    val dayOfWeek: DayOfWeek,
    val status: MustDoStatus,
)
