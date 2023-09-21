package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeStudyResponseDto(
    @SerialName("id")
    val userId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("todoContent")
    val mustDo: String,
    @SerialName("leftDays")
    val leftDays: Int,
    @SerialName("grassCount")
    val grassSeedsCount: Int,
    @SerialName("isMaster")
    val isMaster: Boolean
)
