package com.created.domain.model

data class WeeklyMustDo(
    val dayOfWeek: String,
    val id: Long,
    val mustDo: String,
    val status: MustDoStatus,
)
