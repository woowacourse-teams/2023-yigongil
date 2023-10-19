package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStudyRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("numberOfMaximumMembers")
    val peopleCount: Int,
    @SerialName("minimumWeeks")
    val studyDate: Int,
    @SerialName("meetingDaysCountPerWeek")
    val numberOfStudyPerWeek: Int,
    @SerialName("introduction")
    val introduction: String,
)
