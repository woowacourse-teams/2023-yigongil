package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoCreateRequestDto(
    @SerialName("isNecessary")
    val isNecessary: Boolean,
    @SerialName("roundId")
    val roundId: Long,
    @SerialName("content")
    val content: String,
)
