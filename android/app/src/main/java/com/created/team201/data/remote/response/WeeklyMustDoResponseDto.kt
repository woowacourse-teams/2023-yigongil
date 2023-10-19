package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyMustDoResponseDto(
    @SerialName("dayOfWeek")
    val dayOfWeek: String,
    @SerialName("id")
    val id: Long,
    @SerialName("mustDo")
    val mustDo: String?,
    @SerialName("status")
    val status: String,
)
