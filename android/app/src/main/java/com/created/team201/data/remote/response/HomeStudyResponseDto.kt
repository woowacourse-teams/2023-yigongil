package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeStudyResponseDto(
    @SerialName("isMaster")
    val isMaster: Boolean,
    @SerialName("grassCount")
    val grassCount: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("leftDays")
    val leftDays: Int,
    @SerialName("name")
    val name: String,
    @SerialName("todoContent")
    val todoContent: String
)