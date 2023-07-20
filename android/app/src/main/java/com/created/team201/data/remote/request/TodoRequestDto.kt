package com.created.team201.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoRequestDto(
    @SerialName("isNecessary")
    val isNecessary: Boolean,
    @SerialName("isDone")
    val isDone: Boolean,
    @SerialName("content")
    val content: String,
)
