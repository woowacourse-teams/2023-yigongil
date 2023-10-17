package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyStartRequestDto(
    @SerialName("meetingDaysOfTheWeek")
    val meetingDaysOfTheWeek: List<String>,
)
