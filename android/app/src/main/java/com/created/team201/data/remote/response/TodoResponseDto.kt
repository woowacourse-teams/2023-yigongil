package com.created.team201.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("content")
    val content: String?,
    @SerialName("isDone")
    val isDone: Boolean,
)
