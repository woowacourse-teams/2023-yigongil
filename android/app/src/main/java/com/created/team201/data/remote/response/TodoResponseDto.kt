package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponseDto(
    @SerialName("content")
    val content: String,
    @SerialName("id")
    val id: Long,
    @SerialName("isDone")
    val isDone: Boolean,
)
