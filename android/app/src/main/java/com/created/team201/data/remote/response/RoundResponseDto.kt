package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoundResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("number")
    val number: Int,
)
