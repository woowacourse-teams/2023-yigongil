package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStudyResponseDto(
    @SerialName("id")
    val studyId: Long,
    @SerialName("name")
    val studyName: String,
    @SerialName("todoContent")
    val mustDo: String?,
    @SerialName("leftDays")
    val leftDays: Int,
    @SerialName("grassCount")
    val grassSeedsCount: Int,
    @SerialName("isMaster")
    val isMaster: Boolean
)
